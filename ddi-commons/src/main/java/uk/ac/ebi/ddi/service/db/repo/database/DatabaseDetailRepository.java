package uk.ac.ebi.ddi.service.db.repo.database;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.database.DatabaseDetail;

import java.util.List;

/**
 * Created by root on 16.05.17.
 */
@Repository
public interface DatabaseDetailRepository extends CrudRepository<DatabaseDetail,String> {
    @Query("{databaseName : ?0}")
    DatabaseDetail findDatabaseByName(String databaseName);

    @Query(value = "{}", fields = "{image: 0}")
    List<DatabaseDetail> findAllShort();

    @Query("{databaseName : { $in: ?0 }}")
    List<DatabaseDetail> findDatabaseByNames(List<String> dbList);
}
