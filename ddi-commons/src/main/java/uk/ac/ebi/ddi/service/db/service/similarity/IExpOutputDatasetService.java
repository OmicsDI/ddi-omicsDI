package uk.ac.ebi.ddi.service.db.service.similarity;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import uk.ac.ebi.ddi.service.db.model.similarity.ExpOutputDataset;

import java.util.List;

/**
 * Created by mingze on 30/07/15.
 */
public interface IExpOutputDatasetService {

    /**
     * Create a new ExpOutputDataset in the MongoDB
     *
     * @param expOutputDataset the ExpOutputDataset to be inserted in the database
     * @return the inserted dataset
     */
    ExpOutputDataset insert(ExpOutputDataset expOutputDataset);

    /**
     * read a dataset from database by id
     *
     * @param id of the dataset entry in database
     * @return a dataset
     */
    ExpOutputDataset read(ObjectId id);

    /**
     * Read all the expOutput datasets from the database
     *
     * @return A list of datasets
     */
    Page<ExpOutputDataset> readAll(int pageStart, int size);

    /**
     * Update a dataset entry in the database using the new information
     *
     * @param expOutputDataset the new datasetAccess information
     * @return the updated datasetAccess.
     */
    ExpOutputDataset update(ExpOutputDataset expOutputDataset);

    /**
     * Remove a DatasetAccess in the Database using the id.
     *
     * @param id Identifier of the dataset to be removed from the database
     * @return the removed dataset
     */
    ExpOutputDataset delete(ObjectId id);

    ExpOutputDataset readByAccession(String accession, String database);

    boolean isDatasetExist(String accession, String database);

    long getNumberOfDatasets();

    void deleteAll();

    List<ExpOutputDataset> readAllInOneType(String dataType);
}
