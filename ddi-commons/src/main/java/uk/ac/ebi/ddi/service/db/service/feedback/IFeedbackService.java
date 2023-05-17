package uk.ac.ebi.ddi.service.db.service.feedback;

import org.bson.types.ObjectId;
import uk.ac.ebi.ddi.service.db.model.feedback.Feedback;

import java.util.List;

/**
 * Created by gaur on 22/2/17.
 */
public interface IFeedbackService {

    Feedback save(Feedback feedback);

    Feedback read(ObjectId id);

    Feedback update(ObjectId id, Feedback feedback);

    void delete(ObjectId id);

    List<Feedback> readAll();

    List<Feedback> read(Boolean isSatisfied);
}

