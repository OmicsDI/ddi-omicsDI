package uk.ac.ebi.ddi.ebe.ws.dao.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol ypriverol
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Field {

    @JsonProperty("values")
    String[] values = null;

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
