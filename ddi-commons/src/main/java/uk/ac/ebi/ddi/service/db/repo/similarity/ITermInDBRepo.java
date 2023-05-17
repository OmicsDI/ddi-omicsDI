package uk.ac.ebi.ddi.service.db.repo.similarity;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.similarity.TermInDB;

import java.util.List;

/**
 * The Access Repository it give information about the access to any resource in the database and the system.
 *
 * @author mingze
 */
@Repository
public interface ITermInDBRepo extends MongoRepository<TermInDB,ObjectId>{

    @Query("{termAccession: ?0}")
    TermInDB findByNameQuery(String termName);

    @Query("{dataType: ?0}")
    List<TermInDB> findByDataType(String dataType);


    @Query("{'$and':[{dataType: ?0},{IDFCalculated: 'false'}]}")
    List<TermInDB> findUncalculatedTermsByDataType(String dataType);

}
