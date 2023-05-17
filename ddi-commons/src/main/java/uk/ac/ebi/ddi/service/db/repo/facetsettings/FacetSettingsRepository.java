package uk.ac.ebi.ddi.service.db.repo.facetsettings;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.facetsettings.FacetSettings;

/**
 * Created by azorin on 22/06/2017.
 */
@Repository
public interface FacetSettingsRepository extends MongoRepository<FacetSettings,String> {
    @Query("{databaseName : ?0}")
    FacetSettings getFirstByMaxFacetCount (int maxFacetCount);

}
