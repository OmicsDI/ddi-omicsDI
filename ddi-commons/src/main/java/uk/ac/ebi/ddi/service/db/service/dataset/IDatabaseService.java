package uk.ac.ebi.ddi.service.db.service.dataset;

import org.bson.types.ObjectId;
import uk.ac.ebi.ddi.service.db.model.dataset.Database;

import java.util.List;

/**
 * Created by yperez on 26/05/2016.
 */
public interface IDatabaseService {

    /**
     * Create a new DatasetAccess in the MongoDB
     * @return the inserted datasetaccess
     */
    Database save(Database database);

    /**
     * Read a datasetAccess entry from the database
     * @param id of the datasetaccess entry
     * @return A DatasetAccess
     */
    Database read(ObjectId id);

    /**
     * Update a datasetAccess entry in the database using the information of the new datasetAccess
     * @return the updated datasetAccess.
     */
    Database update(ObjectId id, Database database);

    /**
     * Remove a DatasetAccess in the Database using the id.
     * @param id Identifier of the datasetAccess to be removed from the database
     * @return the removed datatsetAccess
     */
    void delete(ObjectId id);

    /**
     * Read a DatasetAccess from the database using the accession and the database
     * @return Return the entry for the dataset
     */
    Database read(String name);

    List<Database> readAll();

}
