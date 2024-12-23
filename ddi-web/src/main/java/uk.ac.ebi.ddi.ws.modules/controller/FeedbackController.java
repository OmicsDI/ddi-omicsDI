package uk.ac.ebi.ddi.ws.modules.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.ddi.service.db.model.feedback.Feedback;
import uk.ac.ebi.ddi.service.db.service.feedback.IFeedbackService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * Created by gaur on 22/2/17.
 */
@Tag(name = "feedback", description = "get feedback about search results")
@Controller
@RequestMapping(value = "/feedback")
@Hidden
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FeedbackController {

    @Autowired
    IFeedbackService feedbackService;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/saveFeedback", method = PUT)
    @ResponseStatus(HttpStatus.OK) // 200
    @ResponseBody
    public void saveFeedback(@RequestBody Feedback feedback, HttpServletRequest httpServletRequest) {
        feedback.setUserInfo(httpServletRequest.getRemoteAddr());
        feedbackService.save(feedback);
    }

    //@CrossOrigin
     @Operation(summary = "Retrieve all file feedbacks", description = "Retrieve all feedbacks for search results")
    @RequestMapping(value = "/getAllFeedbacks", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.readAll();
    }

     @Operation(summary = "Retrieve all file feedbacks by satisfaction status", description = "Retrieve all feedbacks for search results by satisfaction status")
    @RequestMapping(value = "/getFeedbackByStatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Feedback> getAllFeedbacksByStatus( @Parameter(description = "satisfaction status of search result, e.g : true")
                                           @RequestParam(value = "isSatisfied", required = true) Boolean isSatisfied) {
        return feedbackService.read(isSatisfied);
    }
}
