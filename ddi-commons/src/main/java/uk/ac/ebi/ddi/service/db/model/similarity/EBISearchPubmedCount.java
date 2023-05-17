package uk.ac.ebi.ddi.service.db.model.similarity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Set;

/**
 * Created by gaur on 27/07/17.
 */
@Document(collection = "similars_ebisearch")
public class EBISearchPubmedCount {

    @Id
    ObjectId _id;

    // Pubmed ID
    Map<String,Set<String>> pubmedDatasetList;

    // INSERTED ID
    String accession;

    // Database ID
    String database;

    Integer pubmedCount;

    public Integer getPubmedCount() {
        return pubmedCount;
    }

    public void setPubmedCount(Integer pubmedCount) {
        this.pubmedCount = pubmedCount;
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

    public Map<String, Set<String>> getPubmedDatasetList() {
        return pubmedDatasetList;
    }

    public void setPubmedDatasetList(Map<String, Set<String>> pubmedDatasetList) {
        this.pubmedDatasetList = pubmedDatasetList;
    }
}
