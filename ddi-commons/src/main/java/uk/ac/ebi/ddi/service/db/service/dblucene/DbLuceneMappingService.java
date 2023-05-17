package uk.ac.ebi.ddi.service.db.service.dblucene;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.model.dblucene.DbLuceneMapping;
import uk.ac.ebi.ddi.service.db.repo.dblucene.IDbLuceneMappingRepo;

import java.util.List;
import java.util.Optional;

/**
 * Created by gaur on 29/3/17.
 */
@Service
public class DbLuceneMappingService implements IDbLuceneMappingService {

    @Autowired
    public IDbLuceneMappingRepo dbLuceneMappingRepo;

    @Override
    public void save(DbLuceneMapping dbLuceneMapping) {
         dbLuceneMappingRepo.save(dbLuceneMapping);
    }

    @Override
    public DbLuceneMapping read(ObjectId id) {
        Optional<DbLuceneMapping> dbLuceneMapping = dbLuceneMappingRepo.findById(id);
        return dbLuceneMapping == null ? null : dbLuceneMapping.get();
    }

    @Override
    public DbLuceneMapping update(ObjectId id, DbLuceneMapping dbLuceneMapping) {
        DbLuceneMapping updateFeedback = read(id);
        if(updateFeedback != null)
            dbLuceneMapping.setId(id);
        return dbLuceneMappingRepo.save(dbLuceneMapping);
    }

    @Override
    public void delete(ObjectId id) {
        dbLuceneMappingRepo.deleteById(id);
    }

    @Override
    public List<DbLuceneMapping> getDatabase(String luceneName){
        return dbLuceneMappingRepo.findByLuceneName(luceneName);
    }

    @Override
    public List<DbLuceneMapping> getLuceneName(String databaseName){
        return dbLuceneMappingRepo.findByDbName(databaseName);
    }

    @Override
    public List<DbLuceneMapping> readAll(){
        return dbLuceneMappingRepo.findAll();
    }

}
