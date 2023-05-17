package uk.ac.ebi.ddi.service.db.repo.logger;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.logger.GenericEvent;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 30/04/2015
 */
@Repository
public interface IEventService extends MongoRepository<GenericEvent, ObjectId>{

}
