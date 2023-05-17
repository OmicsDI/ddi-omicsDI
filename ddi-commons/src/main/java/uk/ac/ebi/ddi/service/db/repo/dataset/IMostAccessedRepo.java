package uk.ac.ebi.ddi.service.db.repo.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.dataset.MostAccessedDatasets;

import java.util.List;

/**
 * Created by gaur on 25/06/17.
 */
@Repository
public interface IMostAccessedRepo extends MongoRepository<MostAccessedDatasets, ObjectId> {

    MostAccessedDatasets getByAccessionAndDatabase(String acession,String database);

    List<MostAccessedDatasets> getByDatabaseIn(List<String> dbList);

}
