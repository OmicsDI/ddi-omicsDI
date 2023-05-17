package uk.ac.ebi.ddi.service.db.repo.feedback;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.ddi.service.db.model.feedback.Feedback;

import java.util.List;

/**
 * Created by gaur on 22/2/17.
 */
@Repository
public interface IFeedbackRepo extends MongoRepository<Feedback, ObjectId> {

        List<Feedback> findByIsSatisfied(Boolean isSatisfied);
}
