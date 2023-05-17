package uk.ac.ebi.ddi.service.db.repo.dblucene;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.dblucene.DbLuceneMapping;

import java.util.List;

/**
 * Created by gaur on 29/3/17.
 */
@Repository
public interface IDbLuceneMappingRepo extends MongoRepository<DbLuceneMapping, ObjectId> {

    List<DbLuceneMapping> findByDbName(String dbName);

    List<DbLuceneMapping> findByLuceneName(String luceneName);
}
