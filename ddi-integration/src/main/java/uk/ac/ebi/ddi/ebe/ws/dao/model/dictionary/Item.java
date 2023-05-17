package uk.ac.ebi.ddi.ebe.ws.dao.model.dictionary;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 */
public class Item {

    String name = null;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
