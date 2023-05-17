package uk.ac.ebi.ddi.service.db.service.similarity;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.exception.DBWriteException;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetShort;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetSimilars;
import uk.ac.ebi.ddi.service.db.model.similarity.DatasetStatInfo;
import uk.ac.ebi.ddi.service.db.model.similarity.ReanalysisData;
import uk.ac.ebi.ddi.service.db.repo.similarity.IDatasetStatInfoRepo;
import uk.ac.ebi.ddi.service.db.utils.Constants;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Fields.fields;

/**
 * Created by mingze on 30/07/15.
 */

@Service
public class DatasetStatInfoService implements IDatasetStatInfoService {


    @Autowired
    private IDatasetStatInfoRepo accessRepo;

    @Autowired
    private MongoOperations mongoOperation;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public DatasetStatInfo insert(DatasetStatInfo datasetStatInfo) {

        DatasetStatInfo oldDatasetStatInfo = accessRepo.findByAccessionQuery(datasetStatInfo.getAccession(), datasetStatInfo.getDatabase());
        if ( oldDatasetStatInfo != null) {
            accessRepo.delete(oldDatasetStatInfo);
        }

        DatasetStatInfo insertedDataset = accessRepo.insert(datasetStatInfo);
        if ((insertedDataset.getId() == null))
            throw new DBWriteException("Inserting fail, no _id assigned");
        return insertedDataset;
    }


    @Override
    public DatasetStatInfo read(ObjectId id) {
        Optional<DatasetStatInfo> datasetStatInfo = accessRepo.findById(id);
        return datasetStatInfo == null ? null : datasetStatInfo.get();
    }

    @Override
    public Page<DatasetStatInfo> readAll(int pageStart, int size) {
        System.out.println("pageStart and size:" + pageStart + "," + size);
//        System.exit(1);
        if(size<1){return null;}
        return accessRepo.findAll(PageRequest.of(pageStart, size));
    }

    @Override
    public List<DatasetStatInfo> readAllInOneType(String dataType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("expDataType").is(dataType));
        return mongoOperation.find(query, DatasetStatInfo.class);
    }

    @Override
    public DatasetStatInfo update(DatasetStatInfo datasetStatInfo) {
        return accessRepo.save(datasetStatInfo);
    }

    @Override
    public DatasetStatInfo delete(ObjectId id) {
        accessRepo.deleteById(id);
        return read(id);
    }

    @Override
    public DatasetStatInfo readByAccession(String accession, String database) {
        if ((accession == null||database == null))
            throw new DBWriteException(" The accession/database to the original resource should contain a string");

        return accessRepo.findByAccessionQuery(accession, database);
    }

    @Override
    public boolean isDatasetExist(String accession, String database) {
        DatasetStatInfo dataset = accessRepo.findByAccessionQuery(accession, database);
        return (dataset != null);
    }

    @Override
    public long getNumberOfDatasets() {
        return (accessRepo.count());
    }

    @Override
    public void deleteAll() {
        accessRepo.deleteAll();
    }

    @Override
    public List<DatasetStatInfo> readAll(){
        return accessRepo.findAll();
    }

    @Override
    public List<DatasetStatInfo> findMultiple(Collection<DatasetShort> datasetShorts) {
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
        return mongoTemplate.find(query, DatasetStatInfo.class);
    }

    @Override
    public List<ReanalysisData> reanalysisCount(){
        Aggregation agg = Aggregation.newAggregation(
                unwind(Constants.SIMILARS_FIELD),
                match(new Criteria(Constants.SIMILARS_RELATIONTYPE).is(Constants.REANALYZED_TYPE)),
                group(fields().and(DSField.DATABASE.key(), DSField.DATABASE.key())
                        .and(DSField.ACCESSION.key(), DSField.ACCESSION.key())).count().as(Constants.TOTAL_FIELD),
                sort(Sort.Direction.DESC, Constants.TOTAL_FIELD),
                project(Constants.TOTAL_FIELD).and(DSField.ACCESSION.key()).as(DSField.ACCESSION.key()).and(DSField.DATABASE.key())
                        .as(DSField.DATABASE.key()));

        AggregationResults<ReanalysisData> groupResults
                = mongoOperation.aggregate(agg, DatasetSimilars.class, ReanalysisData.class);

        return groupResults.getMappedResults();
    }
}
