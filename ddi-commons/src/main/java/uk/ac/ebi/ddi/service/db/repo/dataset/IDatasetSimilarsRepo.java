package uk.ac.ebi.ddi.service.db.repo.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetSimilars;

import java.util.List;

/**
 * Created by yperez on 13/06/2016.
 */
@Repository
public interface IDatasetSimilarsRepo extends MongoRepository<DatasetSimilars,ObjectId>, IDatasetSimilarsRepoExtension {

    @Query(value="{ database : ?0 }", fields ="{database : 1, accession : 1, hashCode: 1, currentStatus: 1}")
    List<DatasetSimilars> findByDatabase(String name);

    @Query("{_id: ?0}")
    DatasetSimilars findByIdDatabaseQuery(ObjectId _id);

/******* where it can be used?? just acc, no db
    @Query("{accession: ?0}")
    List<DatasetSimilars> findByAccession(String accession);
******/
}
