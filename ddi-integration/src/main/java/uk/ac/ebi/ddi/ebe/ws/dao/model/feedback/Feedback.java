package uk.ac.ebi.ddi.ebe.ws.dao.model.feedback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by gaur on 22/2/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feedback {
    @JsonProperty("id")
    String id = null;

    @JsonProperty("isSatisfied")
    Boolean isSatisfied;

    @JsonProperty("message")
    String message;

    @JsonProperty("userInfo")
    String userInfo;

    @JsonProperty("searchQuery")
    String searchQuery;

    public Feedback() {

    }

    public Feedback(@JsonProperty("isSatisfied") Boolean isSatisfied,
                    @JsonProperty("message") String message,
                    @JsonProperty("userInfo") String userInfo,
                    @JsonProperty("searchQuery") String searchQuery) {
        this.isSatisfied = isSatisfied;
        this.message = message;
        this.userInfo = userInfo;
        this.searchQuery = searchQuery;
    }


}
