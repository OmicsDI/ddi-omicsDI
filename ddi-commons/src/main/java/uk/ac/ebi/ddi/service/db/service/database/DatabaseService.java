package uk.ac.ebi.ddi.service.db.service.database;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.model.dataset.Database;
import uk.ac.ebi.ddi.service.db.repo.dataset.IDatabaseRepo;
import uk.ac.ebi.ddi.service.db.service.dataset.IDatabaseService;

import java.util.List;
import java.util.Optional;

/**
 * Created by yperez on 26/05/2016.
 */
@Service
public class DatabaseService implements IDatabaseService {

    @Autowired
    private IDatabaseRepo databaseRepo;

    @Override
    public Database save(Database database) {
        return databaseRepo.save(database);
    }

    @Override
    public Database read(ObjectId id) {
        Optional<Database> database = databaseRepo.findById(id);
        return database == null ? null : database.get();
    }

    @Override
    public Database update(ObjectId id, Database database) {
        Database updateDatabase = read(id);
        if (updateDatabase != null) {
            database.set_id(id);
        }
        return databaseRepo.save(database);
    }

    @Override
    public void delete(ObjectId id) {
        databaseRepo.deleteById(id);
    }

    @Override
    public Database read(String name) {
        return databaseRepo.findByNameQuery(name);
    }

    @Override
    public List<Database> readAll(){
        return databaseRepo.findAll();
    }
}
