package uk.ac.ebi.ddi.geo.model;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "eSummaryResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ESummaryResult {
    private List<DocSum> DocSum = new ArrayList<>();


    public List<DocSum> getDocSum() {
        return this.DocSum;
    }

    public void setDocSum(List<DocSum> docSum) {
        this.DocSum = docSum;
    }
}