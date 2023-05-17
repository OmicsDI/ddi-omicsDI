package uk.ac.ebi.ddi.service.db.repo.similarity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import uk.ac.ebi.ddi.service.db.model.similarity.ReanalysisData;

import java.util.List;

/**
 * Created by gaur on 27/07/17.
 */
public interface IReanalysisRepo extends MongoRepository<ReanalysisData,ObjectId> {

    @Query("{'$and': [{'accession': ?0},{'database': ?1}]}")
    ReanalysisData getReanalysisCount(String accession, String database);

    @Query("{'similars.relationType':?0}")
    List<ReanalysisData> getDatasetsByRelation(String relationValue);
}
