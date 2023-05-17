package uk.ac.ebi.ddi.service.db.service.dataset;


import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import uk.ac.ebi.ddi.service.db.model.aggregate.BaseAggregate;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetShort;
import uk.ac.ebi.ddi.service.db.model.dataset.DbDatasetCount;
import uk.ac.ebi.ddi.service.db.model.dataset.MergeCandidate;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * THis interface allows the creation of and handling of DatasetAccess in the database.
 * @author ypriverol
 *
 */
public interface IDatasetService {

    /**
     * Create a new DatasetAccess in the MongoDB
     * @param datasetResource The new datset access to be save in the database
     * @return the inserted datasetaccess
     */
    Dataset save(Dataset datasetResource);

    /**
     * Read a datasetAccess entry from the database
     * @param id of the datasetaccess entry
     * @return A DatasetAccess
     */
    Dataset read(ObjectId id);

    /**
     * Read all the datasetAccess from the database
     * @return A list of datasetAccess
     */
    Page<Dataset> readAll(int pageStart, int size);

    /**
     * Update a datasetAccess entry in the database using the information of the new datasetAccess
     * @param datasetResource the new datasetAccess information
     * @return the updated datasetAccess.
     */
    Dataset update(ObjectId id, Dataset datasetResource);

    /**
     * Remove a DatasetAccess in the Database using the id.
     * @param id Identifier of the datasetAccess to be removed from the database
     * @return the removed datatsetAccess
     */
    void delete(ObjectId id);

    /**
     * Read a DatasetAccess from the database using the accession and the database
     * @param acc Accession of the DatasetAccess
     * @param database  Database of the DatasetAceess
     * @return Return the entry for the dataset
     */
    Dataset read(String acc, String database);

    /**
     * Find multiple datasets in one request
     * Be aware that this will not report a problem if a dataset could not be found
     * @param datasetShorts
     * @return
     */
    List<Dataset> findMultipleDatasets(Collection<DatasetShort> datasetShorts);

    List<Dataset> readDatasetHashCode(String database);

    Dataset updateCategory(Dataset dataset);

    /**
     * Find all datasets by an specific accession
     * @param accession
     * @return
     */
    List<Dataset> findByAccession(String accession);

    <T extends BaseAggregate> List<T> getAggregationResults(Aggregation aggregation, String collectionName, Class<T> outputType);

    List<Dataset> getSimilarByPubmed(String pubmedId);

    void updateDatasetClaim(String[] databases);

    long getDatasetCount();

    List<Dataset> findByDatabaseBioModels(String database);

    Dataset findByFullDatasetLink(String url);


    /**
     * Find all datasets by an specific accession
     * @param accession
     * @return
     */
    List<Dataset> getBySecondaryAccession(String accession);

    /**
     * Performance optimisation - quick search if secondary accession exists
     * @param accession
     * @return boolean
     */
    Boolean existsBySecondaryAccession(String accession);

    Set<String> getAllSecondaryAccessions();

    List<MergeCandidate> getMergeCandidates(int start, int size);

    Integer getMergeCandidateCount();

    void mergeDatasets(MergeCandidate mergeCandidate);

    long getMergedDatasetCount(String database, String accession);

    List<DbDatasetCount> getDbDatasetsCount();

    void skipMerge(MergeCandidate mergeCandidate);

    void addMultiomics(MergeCandidate mergeCandidate);


    List<Dataset> getPrivateDatasets(String database);

    /*
    * update datasets which are private and not accessible
    *
    * @param database
    * */
    void updatePrivateDatasets(String database);


    /*
    * get all datasets where search domain does not exists
    * @param pageStart
    * @param size
    * @return Page<Dataset>
    *
    * */
    Page<Dataset> getWithoutSearchDomains(int pageStart, int size);

    /**
     * get all datasets by pages with
     *
     * @param pageStart,
     * @param pageSize
     *
     * @return Page<Dataset>
     */
    Page<Dataset> getDatasetPage(int pageStart, int pageSize);

    /*
    * get stream of all datasets
    *
    * @return Stream<Dataset>
    *
    * */
    Stream<Dataset> getAllData();

    Page<Dataset> readDatasetsByDatabase(String database, int pageStart, int size);


}
