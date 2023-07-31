package uk.ac.ebi.ddi.geo.model;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlType(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

    @XmlAttribute(name = "Type")
    private String type;


    private List<String> Content;
    @XmlAttribute(name = "Name")
    private String name;
    @XmlElement(name = "Item")
    private List<Item> item = new ArrayList<>();

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @XmlMixed
    public List<String> getContent() {
        return Content;
    }

    public void setContent(List<String> content) {
        Content = content;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItem() {
        return item;
    }
    public void setItem(List<Item> item) {
        this.item = item;
    }
}