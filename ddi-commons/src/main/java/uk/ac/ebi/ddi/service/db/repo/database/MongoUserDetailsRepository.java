package uk.ac.ebi.ddi.service.db.repo.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.dataset.MongoUser;

/**
 * Created by user on 3/13/2017.
 */
@Repository("mongoUserDetailsRepository")
public interface MongoUserDetailsRepository extends PagingAndSortingRepository<MongoUser, String> , CrudRepository<MongoUser, String> {
    @Query("{userName: ?0}")
    MongoUser findByName(String name);

    @Query("{_id: ?0, 'isPublic':true}")
    MongoUser findPublicById(String name);

    @Query("{UserId: ?0}")
    MongoUser findByUserId(String UserId);

    Page<MongoUser> findByUserIdNotNull(Pageable pageable);
}
