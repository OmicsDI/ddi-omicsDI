package uk.ac.ebi.ddi.service.db.repo.similarity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import uk.ac.ebi.ddi.service.db.model.similarity.Citations;

/**
 * Created by gaur on 20/07/17.
 */
public interface ICitationRepo extends MongoRepository<Citations,ObjectId> {

    @Query("{'$and': [{'accession': ?0},{'database': ?1}]}")
    Citations readCitationCount(String accession, String database);
}
