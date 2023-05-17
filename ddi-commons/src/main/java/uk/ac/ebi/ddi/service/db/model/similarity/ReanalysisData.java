package uk.ac.ebi.ddi.service.db.model.similarity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Created by gaur on 27/07/17.
 */
//@Document(collection = "similars_reanalyis")
@Document(collection = "datasets.similars")
public class ReanalysisData {

    @Id
    ObjectId _id;

    // Pubmed ID
    Set<String> datasets;

    // INSERTED ID
    String accession;

    // Database ID
    String database;

    Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Set<String> getDatasets() {
        return datasets;
    }

    public void setDatasets(Set<String> datasets) {
        this.datasets = datasets;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
