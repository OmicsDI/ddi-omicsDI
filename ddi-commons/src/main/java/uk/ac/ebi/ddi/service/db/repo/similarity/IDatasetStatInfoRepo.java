package uk.ac.ebi.ddi.service.db.repo.similarity;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import uk.ac.ebi.ddi.service.db.model.similarity.DatasetStatInfo;

/**
 * The Access Repository it give information about the access to any resource in the database and the system.
 *
 * @author Mingze
 */

public interface IDatasetStatInfoRepo extends MongoRepository<DatasetStatInfo,ObjectId>{

    @Query("{'$and': [{'accession': ?0},{'database': ?1}]}")
    DatasetStatInfo findByAccessionQuery(String accession, String database);

}
