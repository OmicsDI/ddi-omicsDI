package uk.ac.ebi.ddi.ebe.ws.dao.model.europmc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by gaur on 13/07/17.
 */
public class CitationResponse {

    @JsonProperty("hitCount")
    public Integer count = null;

    @JsonProperty("resultList")
    public Map<String, Citation[]> citations;

    @JsonProperty("nextCursorMark")
    public String cursorMark;


}
