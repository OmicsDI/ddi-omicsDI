package uk.ac.ebi.ddi.service.db.service.enrichment;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import uk.ac.ebi.ddi.service.db.model.enrichment.DatasetEnrichmentInfo;
import uk.ac.ebi.ddi.service.db.model.enrichment.Identifier;

import java.util.List;

/**
 * Created by mingze on 30/07/15.
 */
public interface IEnrichmentInfoService {

    /**
     * Create a new DatasetEnrichmentInfo in the MongoDB
     *
     * @param datasetEnrichmentInfo the Enrichied INSERTED to be inserted in the database
     * @return the inserted dataset
     */
    DatasetEnrichmentInfo insert(DatasetEnrichmentInfo datasetEnrichmentInfo);


    /**
     * Find the latest version of datasetEnrichmentInfo
     * @param accession
     * @param database
     * @return
     */
    DatasetEnrichmentInfo getLatest(String accession, String database);

    /**
     * read a dataset from database by id
     *
     * @param id of the dataset entry in database
     * @return a dataset
     */
    DatasetEnrichmentInfo read(ObjectId id);

    /**
     * Read all the enriched datasets from the database
     *
     * @return A list of datasets
     */
    Page<DatasetEnrichmentInfo> readAll(int pageStart, int size);

    /**
     * Update a dataset entry in the database using the new information
     *
     * @param datasetEnrichmentInfo the new datasetAccess information
     * @return the updated datasetAccess.
     */
    DatasetEnrichmentInfo update(DatasetEnrichmentInfo datasetEnrichmentInfo);

    /**
     * Remove a DatasetAccess in the Database using the id.
     *
     * @param id Identifier of the dataset to be removed from the database
     * @return the removed dataset
     */
    DatasetEnrichmentInfo delete(ObjectId id);

    DatasetEnrichmentInfo readByAccession(String accession, String database);

    List<DatasetEnrichmentInfo> readByAccessionDatabase(String accession, String database);


    boolean isDatasetExist(String accession, String database);

    void deleteAll();

    void updateIdentifiers(Iterable<Identifier> identifiers);

    public List<String> getAdditionalAccession(String accession);

}
