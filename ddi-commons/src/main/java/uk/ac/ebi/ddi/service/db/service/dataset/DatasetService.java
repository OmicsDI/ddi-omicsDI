package uk.ac.ebi.ddi.service.db.service.dataset;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.aggregate.BaseAggregate;
import uk.ac.ebi.ddi.service.db.model.dataset.*;
import uk.ac.ebi.ddi.service.db.repo.dataset.IDatasetRepo;
import uk.ac.ebi.ddi.service.db.utils.Constants;
import uk.ac.ebi.ddi.service.db.utils.DatasetSimilarsType;
import uk.ac.ebi.ddi.service.db.utils.Utilities;


import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;


/**
 * The DatasetAccess reader that implements all the methods to retrieve a dataset, remove it. or find them.
 *
 * @author Yasset Perez-Riverol
 */
@Service
public class DatasetService implements IDatasetService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IDatasetRepo datasetAccessRepo;

    @Autowired
    private IDatasetSimilarsService datasetSimilarsService;

    @Autowired
    private IUnMergeDatasetService unMergeDatasetService;

    @Override
    public Dataset save(Dataset dataset) {
        return datasetAccessRepo.save(dataset);
    }

    @Override
    public Dataset read(ObjectId id) {
        Optional<Dataset> dataset = datasetAccessRepo.findById(id);
        return dataset == null ? null : dataset.get();
    }

    @Override
    public Page<Dataset> readAll(int pageStart, int size) {
        return datasetAccessRepo.findAll(PageRequest.of(pageStart, size));
    }

    @Override
    public Dataset update(ObjectId id, Dataset dataset) {

        Optional<Dataset> existingDatasetOptional = datasetAccessRepo.findById(id);

        if (existingDatasetOptional!= null) {
            Dataset existingDataset = existingDatasetOptional.get();
            existingDataset.setCrossReferences(dataset.getCrossReferences());
            existingDataset.setDescription(dataset.getDescription());
            existingDataset.setCurrentStatus(dataset.getCurrentStatus());
            existingDataset.setAdditional(dataset.getAdditional());
            existingDataset.setAccession(dataset.getAccession());
            existingDataset.setDatabase(dataset.getDatabase());
            existingDataset.setDates(dataset.getDates());
            existingDataset.setName(dataset.getName());
            existingDataset.setFilePath(dataset.getFilePath());
            //TODO : Need to Fix as part of https://www.ebi.ac.uk/panda/jira/browse/OMDI-48 ticket
            Map<String,Set<String>> datasetFilesMap = new HashMap<String,Set<String>>();
            if(!CollectionUtils.isEmpty(dataset.getAdditional().get(DSField.Additional.DATASET_FILE.key())) && dataset.getAdditional().get(DSField.Additional.DATASET_FILE.key()).size() > 0){
                Set<String> datasetFiles = dataset.getAdditional().get(DSField.Additional.DATASET_FILE.key());
                datasetFilesMap.put(dataset.getAccession(),datasetFiles);
                existingDataset.setFiles(datasetFilesMap);
            }
            existingDataset.setScores(dataset.getScores());
            return datasetAccessRepo.save(existingDataset);
        }

        return null;
    }

    @Override
    public void delete(ObjectId id) {
        datasetAccessRepo.deleteById(id);
    }

    @Override
    public Dataset read(String acc, String database) {
        return datasetAccessRepo.findByAccessionDatabaseQuery(acc, database);
    }

    @Override
    public List<Dataset> findMultipleDatasets(Collection<DatasetShort> datasetShorts) {
        if (datasetShorts.isEmpty()) {
            return Collections.emptyList();
        }
        Query query = new Query();
        Criteria criteria = new Criteria();
        List<Criteria> orOperators = new ArrayList<>();
        for (DatasetShort datasetShort : datasetShorts) {
            orOperators.add(new Criteria().andOperator(
                    Criteria.where("accession").is(datasetShort.getAccession()),
                    Criteria.where("database").is(datasetShort.getDatabase()
            )));
        }
        query.addCriteria(criteria.orOperator(orOperators.toArray(new Criteria[0])));
        query.fields()
                .exclude("additional.sample_protocol")
                .exclude("additional.data_protocol")
                .exclude("additional.name_synonyms")
                .exclude("additional.description_synonyms")
                .exclude("additional.sample_synonyms")
                .exclude("additional.data_synonyms");
        return mongoTemplate.find(query, Dataset.class);
    }

    @Override
    public List<Dataset> readDatasetHashCode(String database) {
        return datasetAccessRepo.findByDatabaseHashCode(database);
    }

    @Override
    public Page<Dataset> readDatasetsByDatabase(String database, int pageStart, int size){
        return datasetAccessRepo.findByDatabase(database,PageRequest.of(pageStart, size));
    }

    @Override
    public Dataset updateCategory(Dataset dataset) {

        Dataset existingDataset = read(dataset.getId());
        existingDataset.setAccession(dataset.getCurrentStatus());
        return datasetAccessRepo.save(existingDataset);
    }

    @Override
    public List<Dataset> findByAccession(String accession) {
        return datasetAccessRepo.findByAccession(accession);
    }

    @Override
    public <T extends BaseAggregate> List<T> getAggregationResults(Aggregation aggregation, String collectionName, Class<T> outputType)
    {
        AggregationResults<T> output
                = mongoTemplate.aggregate(aggregation, collectionName,outputType);

        return output.getMappedResults();
    }

    public List<Dataset> getSimilarByPubmed(String pubmedId){
        return datasetAccessRepo.findByCrossReferencesPubmed(pubmedId);
    }

    public void updateDatasetClaim(String[] databases){
        Query searchQuery = new Query();
        searchQuery.addCriteria(new Criteria(DSField.DATABASE.key()).in(databases));

        HashSet<String> claimableStatus = new HashSet<String>();
        claimableStatus.add("true");

        Update updateClaim = new Update();
        updateClaim.set(DSField.IS_CLAIMABLE.key(), true).set(Constants.ADDITIONAL_CLAIMABLE, claimableStatus);

        mongoTemplate.updateMulti(searchQuery,updateClaim,Constants.DATASET_COLLECTION);

    }

    public long getDatasetCount(){
        return datasetAccessRepo.count();
    }

    @Override
    public List<Dataset> findByDatabaseBioModels(String database) {
        return datasetAccessRepo.findByDatabaseBioModels(database);
    }

    @Override
    public Dataset findByFullDatasetLink(String url){
        return datasetAccessRepo.findByFullDatasetLink(url);
    }

    @Override
    public List<Dataset> getBySecondaryAccession(String accession){
        // Secondary accession always start with an accession and following by ~<url>
        // So, it will be faster when use ^ regex
        return datasetAccessRepo.getBySecondaryAccession("^" + accession);
    }

    Set<String> secondaryAccessionsSet = null;
    ReentrantLock lock = new ReentrantLock();

    @Override
    public Boolean existsBySecondaryAccession(String accession){
        lock.lock();
        try {
            if (null == this.secondaryAccessionsSet) {
                this.secondaryAccessionsSet = datasetAccessRepo.getSecondaryAccessions();
            }
            return (this.secondaryAccessionsSet.contains(accession));
        }finally{
            lock.unlock();
        }
    }

    @Override
    public Set<String> getAllSecondaryAccessions(){
        return(datasetAccessRepo.getSecondaryAccessions());
    }

    @Override
    public List<MergeCandidate> getMergeCandidates(int start, int size){

        List<MergeCandidate> results = datasetAccessRepo.getMergeCandidates(start,size);

        for(MergeCandidate m: results){
            Dataset ds = datasetAccessRepo.findByAccessionDatabaseQuery(m.getAccession(), m.getDatabase());
            if(null!=ds) {
                m.setName(ds.getName());
            }
            for(DatasetShort s : m.getSimilars()){
                Dataset ds1 = datasetAccessRepo.findByAccessionDatabaseQuery(s.getAccession(), s.getDatabase());
                if(null!=ds1) {
                    s.setName(ds1.getName());
                }
            }
        }

        return results;
    }

    @Override
    public Integer getMergeCandidateCount(){
        return datasetAccessRepo.getMergeCandidateCount();
    }

    @Override
    public void mergeDatasets(MergeCandidate mergeData){

        datasetAccessRepo.mergeDataset(mergeData);
        //datasetAccessRepo.deleteMergeCandidte(mergeData.getDatabase(), mergeData.getAccession());

        for(DatasetShort d : mergeData.getSimilars()) {
            Dataset ds = datasetAccessRepo.findByAccessionDatabaseQuery(d.getAccession(), d.getDatabase());
            String link = ds.getAdditional().get("full_dataset_link") != null ? ds.getAdditional().get("full_dataset_link").iterator().next() :"";
            String accessionLink = d.getAccession() + "~" + link;
            datasetAccessRepo.addSecondaryAccession(mergeData.getDatabase(), mergeData.getAccession(), accessionLink);

            //datasetAccessRepo.delete(d.getDatabase(), d.getAccession());
            UnMergeDatasets unMergeDatasets = new UnMergeDatasets();
            MergedDataset mergedDataset = new MergedDataset(ds);
            unMergeDatasets.setDataset(mergedDataset);
            unMergeDatasets.setMasterAccession(mergeData.getAccession());
            unMergeDatasets.setMasterDatabase(mergeData.getDatabase());
            unMergeDatasetService.save(unMergeDatasets);
            datasetAccessRepo.deleteMergeCandidte(d.getDatabase(), d.getAccession());
            datasetAccessRepo.delete(d.getDatabase(), d.getAccession());
        }


    }

    @Override
    public long getMergedDatasetCount(String database, String accession) {
        return datasetAccessRepo.getMergedDatasetCount(database,accession);
    }

    @Override
    public List<DbDatasetCount> getDbDatasetsCount(){
        Aggregation agg = newAggregation(
                group("database").count().as("dbCount")
        );
        //Convert the aggregation result into a List
        AggregationResults<DbDatasetCount> groupResults
                = mongoTemplate.aggregate(agg,Dataset.class, DbDatasetCount.class);
        List<DbDatasetCount> result = groupResults.getMappedResults();

        return result;

    }

    public void skipMerge(MergeCandidate mergeCandidate){
        for(DatasetShort d : mergeCandidate.getSimilars()) {
            datasetAccessRepo.deleteMergeCandidte(d.getDatabase(), d.getAccession());
        }
    }

    public List<Dataset> getPrivateDatasets(String database){
        return datasetAccessRepo.getPrivateByDatabase(database);
    }

    public void addMultiomics(MergeCandidate mergeCandidate){
        for (DatasetShort dt:mergeCandidate.getSimilars()
             ) {
            Dataset dataset = datasetAccessRepo.findByAccessionDatabaseQuery(dt.getAccession(), dt.getDatabase());
            if (dataset != null) {
                Utilities.addAdditionalField(dataset, DSField.Additional.OMICS.key(), Constants.MULTIOMICS_TYPE);
                save(dataset);
                datasetSimilarsService.addDatasetSim(dataset, mergeCandidate.getSimilars(),
                        DatasetSimilarsType.OTHER_OMICS_DATA.getType());
            }
        }
        skipMerge(mergeCandidate);
    }

    public void updatePrivateDatasets(String database){
        Query query  = new Query(Criteria.where(DSField.DATABASE.key()).is(database).
                and("$where").is("this.accession == this.name"));

        Update update = new Update();
        update.addToSet("additional.isPrivate","true");

        mongoTemplate.updateMulti(query, update,Constants.DATASET_COLLECTION);
    }

    @Override
    public Page<Dataset> getWithoutSearchDomains(int pageStart, int size){
        return datasetAccessRepo.getByDatasetWithoutSearchDomain(PageRequest.of(pageStart, size));
    }

    /*
    * get streams of all datasets
    * */
    public Stream<Dataset> getAllData(){
        return datasetAccessRepo.findAllAndStream();
    }

    /*
    * get all datasets pages by page number and page size
    *
    *@param pageStart
    *@param pageSize
    *
    *@return Page<Dataset>
    *
    * */
    public Page<Dataset> getDatasetPage(int pageStart, int pageSize){
        if(pageSize > 100){
            pageSize = 100;
        }
        return datasetAccessRepo.findAll(PageRequest.of(pageStart, pageSize));
    }


}
