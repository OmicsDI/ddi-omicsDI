package uk.ac.ebi.ddi.service.db.service.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.model.database.DatabaseDetail;
import uk.ac.ebi.ddi.service.db.repo.database.DatabaseDetailRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 16.05.17.
 */
@Service
public class DatabaseDetailService {

    DatabaseDetailRepository databaseDetailRepository;

    private Map<String, String> nameToSource = new ConcurrentHashMap<>();
    private Map<String, String> sourceToName = new ConcurrentHashMap<>();

    @Autowired
    public DatabaseDetailService(DatabaseDetailRepository databaseDetailRepository) {
        this.databaseDetailRepository = databaseDetailRepository;
        this.getDatabaseList();
    }

    public List<DatabaseDetail> getDatabaseList() {
        List<DatabaseDetail> iterable = databaseDetailRepository.findAllShort();
        computeResolvers(iterable);
        return iterable;
    }

    private void  computeResolvers(List<DatabaseDetail> databaseDetails){
        for (DatabaseDetail databaseDetail : databaseDetails) {
            String dbName = nameToSource.get(databaseDetail.getDatabaseName());
            if (dbName == null || !dbName.equals(databaseDetail.getSource())) {
                nameToSource.put(databaseDetail.getDatabaseName(), databaseDetail.getSource());
            }
            String source = sourceToName.get(databaseDetail.getSource());
            if (source == null || !source.equals(databaseDetail.getDatabaseName())) {
                sourceToName.put(databaseDetail.getSource(), databaseDetail.getDatabaseName());
            }
        }
    }

    public DatabaseDetail findDatabaseById(String id) {
        Optional<DatabaseDetail> databaseDetailOptional = databaseDetailRepository.findById(id);
        return databaseDetailOptional == null ? new DatabaseDetail() : databaseDetailOptional.get();
    }

    public DatabaseDetail findDatabaseByName(String databaseName) {

        DatabaseDetail databaseDetail = databaseDetailRepository.findDatabaseByName(databaseName);
        return databaseDetail == null ? new DatabaseDetail() : databaseDetail;
    }

    public void saveDatabase(DatabaseDetail databaseDetail) {
        databaseDetailRepository.save(databaseDetail);
    }

    //** source -> name
    public String retriveAnchorName(String value) {
        return sourceToName.entrySet().stream()
                           .filter(e -> e.getKey().equalsIgnoreCase(value))
                           .map(Map.Entry::getValue).findFirst().orElse(null);
    }

    //** source -> name
    public String retriveSolrName(String value){
        return nameToSource.get(value);
    }

    public List<DatabaseDetail> findModelExchangeDatabase(List<String> databases){
        return databaseDetailRepository.findDatabaseByNames(databases);
    }
}
