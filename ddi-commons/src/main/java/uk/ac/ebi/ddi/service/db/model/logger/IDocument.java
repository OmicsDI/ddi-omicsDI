package uk.ac.ebi.ddi.service.db.model.logger;

import org.bson.types.ObjectId;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 30/04/2015
 */
public interface IDocument {

    /**
     * Every Document should contain an Id
     * @return
     */
    ObjectId getId();

    /**
     * All the documents should be able to export to String
     * @return
     */
    String toString();

    /**
     * This function allows to retrieve the type of the Object, is really useful
     * for query purpose where the user doesn't known the original type of the object.
     * @return
     */
    String getCategory();

}
