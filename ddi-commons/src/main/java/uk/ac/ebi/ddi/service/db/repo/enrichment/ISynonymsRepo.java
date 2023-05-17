package uk.ac.ebi.ddi.service.db.repo.enrichment;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.enrichment.Synonym;

/**
 * The Access Repository it give information about the access to any resource in the database and the system.
 *
 * @author Mingze
 */
@Repository
public interface ISynonymsRepo extends MongoRepository<Synonym,ObjectId>{


    @Query("{label : ?0}")
    Synonym findByLabelQuery(String label);



}
