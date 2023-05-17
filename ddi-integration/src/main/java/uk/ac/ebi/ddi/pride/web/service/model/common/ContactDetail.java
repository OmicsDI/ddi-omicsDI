package uk.ac.ebi.ddi.pride.web.service.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jadianes
 * @author ypriverol
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class ContactDetail {

    @JsonProperty("email")
    public String email;               // the contact's email address,

    @JsonProperty("title")
    public String title;               // the title of the contact person

    @JsonProperty("firstName")
    public String firstName;           //the first name of the contact person

    @JsonProperty("lastName")
    public String lastName;            // the last/family name of the contact person,

    @JsonProperty("affiliation")
    public String affiliation;         // the affiliation of the contact person
}
