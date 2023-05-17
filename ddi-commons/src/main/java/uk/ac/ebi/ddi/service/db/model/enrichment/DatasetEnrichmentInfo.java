package uk.ac.ebi.ddi.service.db.model.enrichment;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.ddi.service.db.model.logger.AbstractDocument;

import java.io.Serializable;
import java.util.*;


/**
 * This class control an access to the System using the same headers from a HttpRequest in the web-service
 * The current file control the IP, region, request size, user and date.
 * @author Yasset Perez-Riverol
 */
@Document(collection = "enrichment.enrichedDataset")
public class DatasetEnrichmentInfo extends AbstractDocument implements Serializable{

    private static final long serialVersionUID = 1326887243102331825L;

    private String accession;
    private String database;
    private Date enrichTime;
    private String status;

    // Key is the attribute with all the synonyms for that key
    Map<String, List<WordInField>> synonyms;

    // Original value of the attribute.
    Map<String, String> originalAttributes;

    @PersistenceConstructor
    public DatasetEnrichmentInfo(String accession, String database) {
        this.accession= accession;
        this.database= database;
    }

    public void setEnrichTime(Date enrichTime) {
        this.enrichTime = enrichTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccession() {
        return accession;
    }

    public Date getEnrichTime() {
        return enrichTime;
    }

    public String getStatus() {
        return status;
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

    public Map<String, List<WordInField>> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Map<String, List<WordInField>> synonyms) {
        this.synonyms = synonyms;
    }

    public Map<String, String> getOriginalAttributes() {
        return originalAttributes;
    }

    public void setOriginalAttributes(Map<String, String> originalAttributes) {
        this.originalAttributes = originalAttributes;
    }

    public void addNewAttributeValue(String key, String value){
        if(originalAttributes == null)
            originalAttributes = new HashMap<>();
        originalAttributes.put(key, value);
    }

    public void addSynonymstoAttribute(String key, List<WordInField> synonyms){
        if(this.synonyms == null)
            this.synonyms = new HashMap<>();
        this.synonyms.put(key, synonyms);
    }

    public void addSynonymstoAttribute(String key, WordInField synonym){
        if(this.synonyms != null)
            this.synonyms = new HashMap<>();
        if(!this.synonyms.containsKey(key))
            this.synonyms.put(key, new ArrayList<WordInField>());
        List<WordInField> list = this.synonyms.get(key);
        list.add(synonym);
        this.synonyms.put(key, list);
    }

    @Override
    public String toString() {
        return "DatasetEnrichmentInfo{" +
                "accession='" + accession + '\'' +
                ", database='" + database + '\'' +
                ", enrichTime=" + enrichTime +
                ", status='" + status + '\'' +
                ", synonyms=" + synonyms +
                ", originalAttributes=" + originalAttributes +
                '}';
    }
}
