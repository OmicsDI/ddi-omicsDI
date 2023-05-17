package uk.ac.ebi.ddi.pride.web.service.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ypriverol
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class ModifiedLocation {

    @JsonProperty("modification")
    public String modification;

    @JsonProperty("location")
    public int location;
}
