package uk.ac.ebi.ddi.ebe.ws.dao.model.dataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.Term;

import java.util.Arrays;

/**
 * @author  ypriverol Yasset Perez-Riverol
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TermResult {

    @JsonProperty("topTerms")
    Term[] terms;

    @JsonProperty("totalTermCount")
    int totalTermCount;

    public Term[] getTerms() {
        return terms;
    }

    public void setTerms(Term[] terms) {
        this.terms = terms;
    }

    @Override
    public String toString() {
        return "TermResult{" +
                "terms=" + Arrays.toString(terms) +
                ", totalTermCount=" + totalTermCount +
                '}';
    }
}
