package uk.ac.ebi.ddi.service.db.service.similarity;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetShort;
import uk.ac.ebi.ddi.service.db.model.similarity.DatasetStatInfo;
import uk.ac.ebi.ddi.service.db.model.similarity.ReanalysisData;

import java.util.Collection;
import java.util.List;

/**
 * Created by mingze on 30/07/15.
 */
public interface IDatasetStatInfoService {

    /**
     * Create a new DatasetStatInfo in the MongoDB
     *
     * @param datasetStatInfo the DatasetStatInfo to be inserted in the database
     * @return the inserted dataset
     */
    DatasetStatInfo insert(DatasetStatInfo datasetStatInfo);

    /**
     * read a dataset from database by id
     *
     * @param id of the dataset entry in database
     * @return a dataset
     */
    DatasetStatInfo read(ObjectId id);

    /**
     * Read all the expOutput datasets from the database
     *
     * @return A list of datasets
     */
    Page<DatasetStatInfo> readAll(int pageStart, int size);

    /**
     * Update a dataset entry in the database using the new information
     *
     * @param datasetStatInfo the new datasetAccess information
     * @return the updated datasetAccess.
     */
    DatasetStatInfo update(DatasetStatInfo datasetStatInfo);

    /**
     * Remove a DatasetAccess in the Database using the id.
     *
     * @param id Identifier of the dataset to be removed from the database
     * @return the removed dataset
     */
    DatasetStatInfo delete(ObjectId id);

    DatasetStatInfo readByAccession(String accession, String database);

    boolean isDatasetExist(String accession, String database);

    long getNumberOfDatasets();

    void deleteAll();

    List<DatasetStatInfo> readAllInOneType(String dataType);

    List<DatasetStatInfo> readAll();

    List<DatasetStatInfo> findMultiple(Collection<DatasetShort> datasetShorts);

    List<ReanalysisData> reanalysisCount();
}
