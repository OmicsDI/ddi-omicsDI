package uk.ac.ebi.ddi.service.db.repo.logger;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.logger.DatasetResource;

/**
 * DatasetResource MongoDB instance that extend the MongoDBRepository
 *
 * @author ypriverol
 */
@Repository
public interface IDatasetResourceRepo extends MongoRepository<DatasetResource,ObjectId>{

    @Query("{'$and':[{accession : ?0}, {database : ?1}]}")
    DatasetResource findByAccessionDatabaseQuery(String acc, String database);

    @Query("{_id: ?0}")
    DatasetResource findByIdDatabaseQuery(ObjectId _id);

}
