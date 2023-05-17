package uk.ac.ebi.ddi.ebe.ws.dao.model.dataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.Entry;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimilarResult {

    @JsonProperty("entries")
    Entry[] entries;

    public Entry[] getEntries() {
        return entries;
    }

    public void setEntries(Entry[] entries) {
        this.entries = entries;
    }
}
