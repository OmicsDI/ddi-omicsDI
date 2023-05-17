package uk.ac.ebi.ddi.service.db.utils;

import uk.ac.ebi.ddi.service.db.model.logger.DatasetResource;
import uk.ac.ebi.ddi.service.db.model.logger.GenericEvent;
import uk.ac.ebi.ddi.service.db.model.logger.GenericResource;
import uk.ac.ebi.ddi.service.db.model.logger.HttpEvent;


/**
 * Define the document record categories for mongoDB, in document oriented is better to define the category for
 * general filters in the entire collection
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * <p/>
 */
public enum CategoryType {

    GENERIC_RESOURCE("GenericResource", GenericResource.class),

    GENERIC_EVENT("GenericEvent", GenericEvent.class),

    HTTP_EVENT("HttpEvent", HttpEvent.class),

    DATASET_RESOURCE("DatasetResource", DatasetResource.class);


    private String category;

    private Class originalClass;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Class getOriginalClass() {
        return originalClass;
    }

    public void setOriginalClass(Class originalClass) {
        this.originalClass = originalClass;
    }

    CategoryType(String category, Class originalClass) {
        this.category = category;
        this.originalClass = originalClass;
    }
}
