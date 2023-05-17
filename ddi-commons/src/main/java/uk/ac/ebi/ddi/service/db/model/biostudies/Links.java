package uk.ac.ebi.ddi.service.db.model.biostudies;

import java.util.List;

public class Links {

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attributes> attributes) {
        this.attributes = attributes;
    }

    List<Attributes> attributes;
}
