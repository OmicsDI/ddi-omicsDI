package uk.ac.ebi.ddi.annotation.service.dataset;

import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import uk.ac.ebi.ddi.annotation.utils.DatasetUtils;
import uk.ac.ebi.ddi.annotation.utils.Utils;
import uk.ac.ebi.ddi.cache.CacheClient;
import uk.ac.ebi.ddi.ddidomaindb.database.DB;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetSimilars;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetStatus;
import uk.ac.ebi.ddi.service.db.model.dataset.SimilarDataset;
import uk.ac.ebi.ddi.service.db.model.publication.PublicationDataset;
import uk.ac.ebi.ddi.service.db.service.dataset.IDatasetService;
import uk.ac.ebi.ddi.service.db.service.dataset.IDatasetSimilarsService;
import uk.ac.ebi.ddi.service.db.service.dataset.IDatasetStatusService;
import uk.ac.ebi.ddi.service.db.service.logger.IHttpEventService;
import uk.ac.ebi.ddi.service.db.service.publication.IPublicationDatasetService;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;
import uk.ac.ebi.ddi.service.db.utils.DatasetSimilarsType;
import uk.ac.ebi.ddi.similarityCalculator.utils.SimilarityConstants;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2016
 */
public class DDIDatasetAnnotationService {

    @Autowired
    IDatasetService datasetService;

    @Autowired
    IDatasetStatusService statusService;

    @Autowired
    IPublicationDatasetService publicationService;

    @Autowired
    IDatasetSimilarsService similarsService;

    @Autowired
    IHttpEventService httpEventService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DDIDatasetAnnotationService.class);

    private static final String CACHE_NAME = "DDIDatasetAnnotation";

    static {
        CacheClient.loadCaches(CACHE_NAME);
    }

    /**
     * This function looks for individual datasets and check if they are in the database and if they needs to
     * be updated.
     *
     * @param dataset
     */
    @Deprecated
    public void insertDataset(Entry dataset) {
        Dataset dbDataset = DatasetUtils.transformEntryDataset(dataset);
        dbDataset = Utils.replaceTextCase(dbDataset);
        Dataset currentDataset = datasetService.read(dbDataset.getAccession(), dbDataset.getDatabase());
        /*LOGGER.info("dates during insertion of "+ currentDataset.getAccession()+"are " +
                currentDataset.getDates().keySet()
                .stream().map(dt -> {
            LOGGER.info("dates during insertion of " + currentDataset.getAccession() +
                    currentDataset.getDates().get(dt).iterator().next());
            return currentDataset.getDates().get(dt);
        }));*/
        if (currentDataset == null) {
            insertDataset(dbDataset);
        } else if (currentDataset.getInitHashCode() != dbDataset.getInitHashCode()) {
            updateDataset(currentDataset, dbDataset);
        }
    }

    /**
     * THis insert use a fixed database Name and not the one provided by the user
     * @param dataset Dataset Entry from the XML
     * @param databaseName database name provided by the users
     */
    public void insertDataset(Entry dataset, String databaseName) {
        Dataset dbDataset = DatasetUtils.transformEntryDataset(dataset, databaseName);
        Utils.replaceTextCase(dbDataset);
        Dataset currentDataset = datasetService.read(dbDataset.getAccession(), dbDataset.getDatabase());

        if (currentDataset != null) {
            if (currentDataset.getDates() != null) {
                for (String date : currentDataset.getDates().keySet()) {
                    LOGGER.info("dates during insertion of " + currentDataset.getAccession() + "with key" + date
                            + "are " + currentDataset.getDates().get(date));
                }
                if (!currentDataset.getDates().isEmpty() && currentDataset.getDates().containsKey("publication")) {
                    LOGGER.info("dates of " + currentDataset.getId() + "are " +
                            currentDataset.getDates().get("publication").toString());
                }
            }

            if (currentDataset.getInitHashCode() != dbDataset.getInitHashCode() ||
                    !currentDataset.getAdditionalField(DSField.Additional.OMICS.key()).
                            containsAll(dbDataset.getAdditionalField(DSField.Additional.OMICS.key()))
                   ) {
                LOGGER.info("Inithashcode of " + dbDataset.getAccession() + " is changed, previous: {}, current: {}",
                        currentDataset.getInitHashCode(), dbDataset.getInitHashCode());

                updateDataset(currentDataset, dbDataset);
            }
        } else {
            LOGGER.info("inserting dataset as dataset is not available");
            LOGGER.info("dataset is " + dbDataset.toString());
            insertDataset(dbDataset);
        }
    }

    private void updateDataset(Dataset currentDataset, Dataset newDataset) {

        Dataset dbDataset = datasetService.update(currentDataset.getId(), newDataset);
        dbDataset.setInitHashCode(newDataset.getInitHashCode());
        //
        if (dbDataset.getId() != null) {
            datasetService.save(dbDataset);
            statusService.save(new DatasetStatus(dbDataset.getAccession(), dbDataset.getDatabase(),
                    dbDataset.getInitHashCode(), getDate(), DatasetCategory.INSERTED.getType()));
        }
    }

    public void annotateDataset(Dataset exitingDataset) {
        if (!exitingDataset.getCurrentStatus().equalsIgnoreCase(DatasetCategory.DELETED.getType())) {
            exitingDataset.setCurrentStatus(DatasetCategory.UPDATED.getType());
        }
        datasetService.update(exitingDataset.getId(), exitingDataset);
        if (!DatasetUtils.getCrossReference(exitingDataset, DSField.CrossRef.PUBMED.key()).isEmpty()) {
            for (String pubmedId: DatasetUtils.getCrossReference(exitingDataset, DSField.CrossRef.PUBMED.key())) {
                //Todo: In the future we need to check for providers that have multiple omics already.
                try {
                    PublicationDataset publicationDataset = publicationService.read(exitingDataset.getAccession(),
                            exitingDataset.getDatabase());
                    if (publicationDataset != null && !publicationDataset.getPubmedId().equals(pubmedId)) {
                        publicationService.save(new PublicationDataset(pubmedId, exitingDataset.getAccession(),
                                exitingDataset.getDatabase(),
                                DatasetUtils.getFirstAdditional(exitingDataset, DSField.Additional.OMICS.key()))
                        );
                    }
                } catch (Exception ex) {
                    LOGGER.info("exception while annotation data with publication Id "+pubmedId+" and accession "+exitingDataset.getAccession(), ex.getMessage().toString());
                }
            }
        }
    }

    public List<PublicationDataset> getPublicationDatasets() {
        return publicationService.readAll();
    }

    public void enrichedDataset(Dataset existingDataset) {
        if (!existingDataset.getCurrentStatus().equalsIgnoreCase(DatasetCategory.DELETED.getType())) {
            existingDataset.setCurrentStatus(DatasetCategory.ENRICHED.getType());
        }
        datasetService.update(existingDataset.getId(), existingDataset);
    }

    public void updateDeleteStatus(Dataset dataset) {
        Dataset existingDataset = datasetService.read(dataset.getId());
        updateStatus(existingDataset, DatasetCategory.DELETED.getType());
    }

    private void updateStatus(Dataset dbDataset, String status) {
        dbDataset.setCurrentStatus(status);
        dbDataset = datasetService.update(dbDataset.getId(), dbDataset);
        if (dbDataset.getId() != null) {
            statusService.save(new DatasetStatus(dbDataset.getAccession(), dbDataset.getDatabase(),
                    dbDataset.getInitHashCode(), getDate(), status)
            );
        }
    }

    public List<Dataset> getAllDatasetsByDatabase(String databaseName) {
        return datasetService.readDatasetHashCode(databaseName);
    }



    public Dataset getDataset(String accession, String database) {
        return datasetService.read(accession, database);
    }

    /**
     * This function transform an Entry in the XML file into a dataset in the database
     * and add then to the database.
     * @param dbDataset
     */
    private void insertDataset(Dataset dbDataset) {
        dbDataset = datasetService.save(dbDataset);
        if (dbDataset.getId() != null) {
            statusService.save(new DatasetStatus(dbDataset.getAccession(), dbDataset.getDatabase(),
                    dbDataset.getInitHashCode(), getDate(), DatasetCategory.INSERTED.getType())
            );
        }
    }

    public Integer findDataset(Entry dataset) {
        Dataset dbDataset = datasetService.read(dataset.getAcc(), dataset.getRepository());
        if (dbDataset != null) {
            return dbDataset.getInitHashCode();
        }
        return null;

    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat(SimilarityConstants.DATE_FORMAT_YYYY);
        return dateFormat.format(new java.util.Date());
    }

    public void updateDataset(Dataset dataset) {
        datasetService.update(dataset.getId(), dataset);
    }

    /**
     * Find a dataset by the Accession
     * @param dbKey db accession
     * @return List of Datasets.
     */
    public List<Dataset> getDataset(String dbKey) {
        return datasetService.findByAccession(dbKey);
    }

    public void updateDatasetSimilars(String accession, String database, Set<SimilarDataset> similars) {
        DatasetSimilars datasetExisting = similarsService.read(accession, database);
        if (datasetExisting == null) {
            datasetExisting = new DatasetSimilars(accession, database, similars);
        } else {
            datasetExisting.setSimilars(similars);
        }
        similarsService.save(datasetExisting);
    }


    public void addDatasetSimilars(Dataset dataset, Set<PublicationDataset> related, String type) {
        DatasetSimilars datasetExisting = similarsService.read(dataset.getAccession(), dataset.getDatabase());
        Set<SimilarDataset> similarDatasets = new HashSet<>();
        for (PublicationDataset publicationDataset: related) {
            if (!publicationDataset.getDatasetID().equalsIgnoreCase(dataset.getAccession())
                    && !publicationDataset.getDatabase().equalsIgnoreCase(dataset.getDatabase())) {
                Dataset datasetRelated = datasetService.read(
                        publicationDataset.getDatasetID(), publicationDataset.getDatabase());
                if (datasetRelated != null) {
                    SimilarDataset similar = new SimilarDataset(datasetRelated, type);
                    similarDatasets.add(similar);
                }
            }
        }
        if (similarDatasets.size() == 0) {
            LOGGER.warn("Adding related datasets to {} with type "
                    + type + ", but none of them were in our database {}", dataset.getAccession(), related);
            return;
        }

        if (datasetExisting == null) {
            datasetExisting = new DatasetSimilars(dataset.getAccession(), dataset.getDatabase(), similarDatasets);
            similarsService.save(datasetExisting);
        } else {
            Set<SimilarDataset> similars = datasetExisting.getSimilars();
            similars.addAll(similarDatasets);
            datasetExisting.setSimilars(similars);
            similarsService.save(datasetExisting);
        }
    }

    public void addGEODatasetSimilars(Dataset dataset, Set<PublicationDataset> related, String type) {
        DatasetSimilars datasetExisting = similarsService.read(dataset.getAccession(), dataset.getDatabase());
        Set<SimilarDataset> similarDatasets = new HashSet<>();
        for (PublicationDataset publicationDataset: related) {
            if (!publicationDataset.getDatasetID().equalsIgnoreCase(dataset.getAccession())) {
                Dataset datasetRelated = datasetService.read(publicationDataset.getDatasetID(),
                        publicationDataset.getDatabase());
                if (datasetRelated != null) {
                    similarDatasets.add(new SimilarDataset(datasetRelated, type));
                }
            }
        }
        if (similarDatasets.size() == 0) {
            LOGGER.warn("Adding related datasets to {} with type "
                            + type + ", but none of them were in our database {}", dataset.getAccession(),
                    related.stream().map(PublicationDataset::getDatasetID).collect(Collectors.toList()));
            return;
        }

        if (datasetExisting == null) {
            datasetExisting = new DatasetSimilars(dataset.getAccession(), dataset.getDatabase(), similarDatasets);
            similarsService.save(datasetExisting);
        } else {
            Set<SimilarDataset> similars = datasetExisting.getSimilars();
            similars.addAll(similarDatasets);
            datasetExisting.setSimilars(similars);
            similarsService.save(datasetExisting);
        }
        LOGGER.info("Added some new related datasets with type " + type + " to {}, {}", dataset.getAccession(),
                similarDatasets.stream().map(x -> x.getSimilarDataset().getAccession()).collect(Collectors.toList()));
    }

    public void addDatasetSimilars(String accession, String database, SimilarDataset similarDataset) {
        DatasetSimilars datasetExisting = similarsService.read(accession, database);
        if (datasetExisting == null) {
            datasetExisting = new DatasetSimilars(accession, database, similarDataset);
            similarsService.save(datasetExisting);
        } else {
            Set<SimilarDataset> similars = datasetExisting.getSimilars();
            similars.add(similarDataset);
            datasetExisting.setSimilars(similars);
            similarsService.save(datasetExisting);
        }
    }


    public void addDatasetReanalysisSimilars(Dataset dataset, Map<String, Set<String>> similarsMap) {

        DatasetSimilars datasetExisting = similarsService.read(dataset.getAccession(), dataset.getDatabase());

        Set<SimilarDataset> similarDatasets = new HashSet<>();
        for (Map.Entry publicationDataset: similarsMap.entrySet()) {
            String databaseKey = (String) publicationDataset.getKey();
            Set<String> values = (Set<String>) publicationDataset.getValue();
            for (String value: values) {
                if (!(databaseKey.equalsIgnoreCase(dataset.getDatabase())
                        && value.equalsIgnoreCase(dataset.getAccession()))) {
                    Dataset datasetRelated = datasetService.read(value, databaseKey);
                    if (datasetRelated != null) {
                        SimilarDataset similar = new SimilarDataset(
                                datasetRelated, DatasetSimilarsType.REANALYSIS_OF.getType());
                        SimilarDataset similar2 = new SimilarDataset(
                                dataset, DatasetSimilarsType.REANALYZED_BY.getType());
                        similarDatasets.add(similar);
                        addDatasetSimilars(datasetRelated.getAccession(), datasetRelated.getDatabase(), similar2);
                    }
                }
            }
        }
        if (datasetExisting == null) {
            datasetExisting = new DatasetSimilars(dataset.getAccession(), dataset.getDatabase(), similarDatasets);
            similarsService.save(datasetExisting);
        } else {
            Set<SimilarDataset> similars = datasetExisting.getSimilars();
            similars.addAll(similarDatasets);
            datasetExisting.setSimilars(similars);
            similarsService.save(datasetExisting);
        }

    }

    public List<DatasetSimilars> getDatasetSimilars() {
        return similarsService.readAll();
    }

    public void removeSimilar(DatasetSimilars dataset) {
        similarsService.delete(dataset);
    }

    /*public List<PublicationDataset> getMutiomicsDatasets()
    {
        return datasetService.getMutiomicsDatasets();
    }*/

    public List<PublicationDataset> getMultiomics() {
        MatchOperation checkPubmedNull = Aggregation.match(new Criteria("crossReferences.pubmed").exists(true).
                andOperator(new Criteria("currentStatus").ne("Deleted")));

        UnwindOperation unwindPubMed = Aggregation.unwind("crossReferences.pubmed");

        GroupOperation groupPubmed = Aggregation.group("crossReferences.pubmed").
                addToSet(new BasicDBObject("ac", "$accession").append("db", "$database")).as("datasets").
                addToSet("additional.omics_type").as("omics_list").count().as("count");

        ProjectionOperation projectStage = Aggregation.project("_id", "count", "datasets", "omics_list").
                and("omics_list").size().as("omics_count").and("_id").as("pubmedId");

        MatchOperation checkMultiomics = Aggregation.match(new Criteria("omics_count").gte(2));

        UnwindOperation unwindDatasets = Aggregation.unwind("datasets");

        ProjectionOperation projectAsPublication = Aggregation.project("pubmedId")
                .and("datasets.ac").as("accession").
                        and("datasets.db").as("database").andExclude("_id");
        Aggregation aggregation = Aggregation.newAggregation(
                checkPubmedNull, unwindPubMed, groupPubmed, projectStage, checkMultiomics, unwindDatasets,
                projectAsPublication);

        return datasetService.getAggregationResults(
                aggregation, "datasets.dataset", PublicationDataset.class);
    }

    public void updateDatasetClaim() {
        String[] sourceDatasets = {DB.PRIDE.getDBName(), DB.METABOLIGHTS.getDBName(),
                DB.METABOLOMEEXPRESS.getDBName(), DB.BIOSTUDIES_ARRAY_EXPRESS.getDBName(), DB.MASSIVE.getDBName(),
                DB.JPOST.getDBName()};

        datasetService.updateDatasetClaim(sourceDatasets);
    }

    public void updateMostAccessed() {
        httpEventService.moreAccessedDataset(20);

    }

    public long getMergedDatasetCount(String database, String accession) {
        return datasetService.getMergedDatasetCount(database, accession);
    }

    public void updatePrivateDataset(String database) {
        datasetService.updatePrivateDatasets(database);
    }

    public void getPrivateDatasets(String database) {
        datasetService.getPrivateDatasets(database);
    }
}
