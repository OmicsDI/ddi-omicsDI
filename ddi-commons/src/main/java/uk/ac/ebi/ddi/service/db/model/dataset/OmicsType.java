package uk.ac.ebi.ddi.service.db.model.dataset;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2016
 */
@Document(collection = "datasets.omicstype")
public class OmicsType {

    String type;

    int numberDatasets;

    public OmicsType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumberDatasets() {
        return numberDatasets;
    }

    public void setNumberDatasets(int numberDatasets) {
        this.numberDatasets = numberDatasets;
    }
}
