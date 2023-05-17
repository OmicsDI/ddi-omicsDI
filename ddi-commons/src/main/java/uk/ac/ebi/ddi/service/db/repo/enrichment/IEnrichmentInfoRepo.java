package uk.ac.ebi.ddi.service.db.repo.enrichment;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.enrichment.DatasetEnrichmentInfo;

import java.util.List;

/**
 * The Access Repository it give information about the access to any resource in the database and the system.
 *
 * @author Mingze
 */
@Repository
public interface IEnrichmentInfoRepo extends MongoRepository<DatasetEnrichmentInfo,ObjectId>{

    @Query("{'$and':[{'accession': ?0},{'database': ?1}, {'status': ?2}]}")
    DatasetEnrichmentInfo findByAccessionDatabaseStatusQuery(String accession, String database, String status);

    @Query("{'$and':[{'accession': ?0},{'database': ?1}]}")
    List<DatasetEnrichmentInfo> findByAccessionDatabaseQuery(String accession, String database);

}
