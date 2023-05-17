package uk.ac.ebi.ddi.service.db.repo.logger;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.ddi.service.db.model.logger.GenericResource;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date   29/04/2015
 */
public interface IResourceRepo extends MongoRepository<GenericResource, ObjectId>{

}
