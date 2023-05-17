package uk.ac.ebi.ddi.service.db.repo.similarity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import uk.ac.ebi.ddi.service.db.model.similarity.EBISearchPubmedCount;

/**
 * Created by gaur on 27/07/17.
 */
public interface IEBISearchPubmedDataRepo extends MongoRepository<EBISearchPubmedCount,ObjectId> {

    @Query("{'$and': [{'accession': ?0}]}")
    EBISearchPubmedCount getSearchCount(String accession);

}
