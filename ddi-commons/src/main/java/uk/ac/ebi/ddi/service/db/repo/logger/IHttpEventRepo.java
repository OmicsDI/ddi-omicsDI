package uk.ac.ebi.ddi.service.db.repo.logger;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.logger.AbstractResource;
import uk.ac.ebi.ddi.service.db.model.logger.HttpEvent;

import java.util.List;

/**
 * The Access Repository it give information about the access to any resource in the database and the system.
 *
 * @author ypriverol
 */
@Repository
public interface IHttpEventRepo extends MongoRepository<HttpEvent,ObjectId>, IHttpEventCustom{

    @Query(value = "{'abstractResource.$accession' : ?0, 'abstractResource.$database' : ?1}", count = true)
    long getNumberEventByHttpEventDataSetResource(String acc, String database);

    @Query(value = "{'abstractResource.$id' : ?0}", count = true)
    long getNumberEventByDataResource(ObjectId _id);

    List<HttpEvent> findByAbstractResource(AbstractResource abstractResource);


}
