package uk.ac.ebi.ddi.ebe.ws.dao.model.europmc;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by gaur on 13/07/17.
 */
public class Citation {

    @JsonProperty("pmid")
    public String pubmedId;
}
