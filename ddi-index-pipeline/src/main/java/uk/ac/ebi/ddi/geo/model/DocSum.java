package uk.ac.ebi.ddi.geo.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "DocSum")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocSum {
    @XmlElement(name = "Item")
    private List<Item> Item = new ArrayList<>();

    @XmlElement(name = "Id")
    private String Id;


    public List<Item> getItem() {
        return Item;
    }
    public void setItem(List<Item> item) {
        this.Item = item;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }
}