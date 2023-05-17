package uk.ac.ebi.ddi.service.db.model.logger;

import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.ddi.service.db.utils.CategoryType;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 30/04/2015
 */
@Document(collection = "logger.event")
public class GenericEvent extends AbstractEvent{

    public GenericEvent() {
        this.category = CategoryType.GENERIC_EVENT.getCategory();
    }

}
