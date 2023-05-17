package uk.ac.ebi.ddi.service.db.service.publication;


import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import uk.ac.ebi.ddi.service.db.model.publication.PublicationDataset;

import java.util.List;

/**
 * THis interface allows the creation of and handling of DatasetAccess in the database.
 * @author ypriverol
 *
 */
public interface IPublicationDatasetService {

    /**
     * Create a new DatasetAccess in the MongoDB
     * @param publicationDataset The new datset access to be save in the database
     * @return the inserted datasetaccess
     */
    PublicationDataset save(PublicationDataset publicationDataset);

    /**
     * Read a datasetAccess entry from the database
     * @param id of the datasetaccess entry
     * @return A DatasetAccess
     */
    PublicationDataset read(ObjectId id);

    /**
     * Read all the datasetAccess from the database
     * @return A list of datasetAccess
     */
    Page<PublicationDataset> readAll(int pageStart, int size);

    /**
     * Read all publication datasets
     * @return
     */
    List<PublicationDataset> readAll();

    /**
     * Update a datasetAccess entry in the database using the information of the new datasetAccess
     * @param datasetResource the new datasetAccess information
     * @return the updated datasetAccess.
     */
    PublicationDataset update(PublicationDataset datasetResource);

    /**
     * Remove a DatasetAccess in the Database using the id.
     * @param id Identifier of the datasetAccess to be removed from the database
     * @return the removed datatsetAccess
     */
    PublicationDataset delete(ObjectId id);

    /**
     * Read a DatasetAccess from the database using the accession and the database
     * @param acc Accession of the DatasetAccess
     * @param database  Database of the DatasetAceess
     * @return Return the entry for the dataset
     */
    PublicationDataset read(String acc, String database);

    List<PublicationDataset> findByPubmedId(String pubmedID);


}
