package uk.ac.ebi.ddi.service.db.repo.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.dataset.UnMergeDatasets;

@Repository
public interface IUnMergeDatasetsRepo extends MongoRepository<UnMergeDatasets,ObjectId> {

    @Query(value = "{'$and':[{data.accession : ?0}, {data.database : ?1}]}", delete = true)
    Long deleteByDataset(String accession, String database);


}
