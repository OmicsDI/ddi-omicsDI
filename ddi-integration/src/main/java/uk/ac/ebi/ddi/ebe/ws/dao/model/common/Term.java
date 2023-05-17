package uk.ac.ebi.ddi.ebe.ws.dao.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ypriverol
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Term {

    @JsonProperty("text")
    String text;

    @JsonProperty("docFreq")
    String frequency;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Term{" +
                "text='" + text + '\'' +
                ", frequency='" + frequency + '\'' +
                '}';
    }
}
