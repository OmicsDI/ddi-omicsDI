package uk.ac.ebi.ddi.service.db.model.dataset;

import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Created by gaur on 27/10/17.
 */
public class ReanalyzedModel {

    public DBRef _id;

    public Dataset data;

}
