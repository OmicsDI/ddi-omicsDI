package uk.ac.ebi.ddi.service.db.service.dblucene;

import org.bson.types.ObjectId;
import uk.ac.ebi.ddi.service.db.model.dblucene.DbLuceneMapping;

import java.util.List;

/**
 * Created by gaur on 29/3/17.
 */
public interface IDbLuceneMappingService {

    void save(DbLuceneMapping dbLuceneMapping);

    DbLuceneMapping read(ObjectId id);

    DbLuceneMapping update(ObjectId id, DbLuceneMapping dbLuceneMapping);

    void delete(ObjectId id);

    List<DbLuceneMapping> readAll();

    List<DbLuceneMapping> getDatabase(String luceneName);

    List<DbLuceneMapping> getLuceneName(String databaseName);

}
