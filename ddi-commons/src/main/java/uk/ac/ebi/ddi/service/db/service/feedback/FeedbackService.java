package uk.ac.ebi.ddi.service.db.service.feedback;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.service.db.model.feedback.Feedback;
import uk.ac.ebi.ddi.service.db.repo.feedback.IFeedbackRepo;

import java.util.List;
import java.util.Optional;

/**
 * Created by gaur on 22/2/17.
 */
@Service
public class FeedbackService implements IFeedbackService{

    @Autowired
    private IFeedbackRepo feedbackRepo;

    @Override
    public Feedback save(Feedback feedback) {
        return feedbackRepo.save(feedback);
    }

    @Override
    public Feedback read(ObjectId id) {
        Optional<Feedback> feedbackRepoById = feedbackRepo.findById(id);
        return feedbackRepoById == null ? null : feedbackRepoById.get();
    }

    @Override
    public Feedback update(ObjectId id, Feedback feedback) {
        Feedback updateFeedback = read(id);
        if(updateFeedback != null)
            feedback.setId(id);
        return feedbackRepo.save(feedback);
    }

    @Override
    public void delete(ObjectId id) {
        feedbackRepo.deleteById(id);
    }

    @Override
    public List<Feedback> read(Boolean isSatisfied) {
        return feedbackRepo.findByIsSatisfied(isSatisfied);
    }

    @Override
    public List<Feedback> readAll(){
        return feedbackRepo.findAll();
    }
}
