package uk.ac.ebi.ddi.ebe.ws.dao.model.dictionary;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * 26/06/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Suggestions {

    @JsonProperty("suggestions")
    Suggestion[] entries;

    public Suggestion[] getEntries() {
        return entries;
    }

    public void setEntries(Suggestion[] entries) {
        this.entries = entries;
    }
}
