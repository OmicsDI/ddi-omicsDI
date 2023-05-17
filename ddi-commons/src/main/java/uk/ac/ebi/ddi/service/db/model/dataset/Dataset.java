package uk.ac.ebi.ddi.service.db.model.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

import java.io.Serializable;
import java.util.*;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 04/05/2016
 */

@Document(collection = "datasets.dataset")
@CompoundIndexes({
        @CompoundIndex(name = "accession_database", def = "{'accession' : 1, 'database': 1}", unique = true)
})
public class Dataset implements Serializable, IDataset{

    @Indexed
    private String accession;
    @Indexed
    private String database;

    int initHashCode;

    @Id
    ObjectId _id;

    // Name
    String name;

    // Description
    String description;

    private Map<String, Set<String>> dates = new HashMap<>();

    // Additional fields
    private Map<String, Set<String>> additional = new HashMap<>();
    //Cross References
    private Map<String, Set<String>> crossReferences = new HashMap<>();

    private Map<String, Set<String>> files = new HashMap<>();

    private Map<String, String> configurations = new HashMap<>();

    private String filePath;

    private String currentStatus;

    public Scores getScores() {
        return scores;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }

    private Scores scores;

    public boolean isClaimable() {
        return isClaimable;
    }

    public void setClaimable(boolean claimable) {
        isClaimable = claimable;
    }

    private boolean isClaimable;

    public Dataset() {
    }

    public Dataset(String accession, String database) {
        this.accession = accession;
        this.database = database;
        this.currentStatus = DatasetCategory.INSERTED.getType();
    }

    /**
     * This Constructor generate an Id for the entity using the MongoDB driver
     * @param accession The access of the experiment in the repository
     * @param database  The id of the repository
     */
    public Dataset(String accession, String database, DatasetCategory category) {
        this(accession, database);
        this.currentStatus = category.getType();
        this.initHashCode = hashCode();
    }

    public Dataset(ObjectId resourceUUID, String accession, String database, DatasetCategory category) {
        this(accession, database);
        this._id = resourceUUID;
        this.currentStatus = category.getType();
        this.initHashCode = hashCode();
    }

    public Dataset(String accession, String database, String name, String description, Map<String, Set<String>> dates,
                   Map<String, Set<String>> additional, Map<String, Set<String>> crossReferences, DatasetCategory category) {
        this(accession, database);
        this.name = name;
        this.description = description;
        this.dates = dates;
        this.additional = additional;
        this.crossReferences = crossReferences;
        this.currentStatus = category.getType();
        this.initHashCode = initHashCode();
    }

    @Deprecated
    public Map<String, Set<String>> getFiles() {
        return files;
    }

    @Deprecated
    public void setFiles(Map<String, Set<String>> files) {
        this.files = files;
    }

    public Set<String> getAllSecondaryAccessions() {
        Set<String> result = new HashSet<>();
        if (additional.get(DSField.Additional.SECONDARY_ACCESSION.key()) != null) {
            result.addAll(additional.get(DSField.Additional.SECONDARY_ACCESSION.key()));
        }
        if (additional.get(DSField.Additional.ADDITIONAL_ACCESSION.key()) != null) {
            result.addAll(additional.get(DSField.Additional.ADDITIONAL_ACCESSION.key()));
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Set<String>> getDates() {
        return dates;
    }

    public void setDates(Map<String, Set<String>> dates) {
        this.dates = dates;
    }

    public Map<String, Set<String>> getAdditional() {
        return additional;
    }

    public void setAdditional(Map<String, Set<String>> additional) {
        this.additional = additional;
    }

    public Map<String, Set<String>> getCrossReferences() {
        return crossReferences;
    }

    public void setCrossReferences(Map<String, Set<String>> crossReferences) {
        this.crossReferences = crossReferences;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getAccession() {
        return accession;
    }

    public String getDatabase() {
        return database;
    }

    public ObjectId getId() {
        return _id;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getInitHashCode() {
        return initHashCode;
    }

    public void setInitHashCode(int initHashCode) {
        this.initHashCode = initHashCode;
    }

    public void addAdditional(String key, Set<String> values){
        additional.put(key, values);
    }
    public void addCrossReferences(String key, Set<String> values){
        crossReferences.put(key, values);
    }

    public Map<String, String> getConfigurations() {
        return configurations;
    }

    public void addCrossReferenceValue(String key, String value) {
        Map<String, Set<String>> fields = getCrossReferences();
        if (key != null && value != null) {
            Set<String> values = new HashSet<>();
            if (fields.containsKey(key)) {
                values = fields.get(key);
            }
            values.add(value);
            fields.put(key, values);
            setCrossReferences(fields);
        }
    }

    public Set<String> getCrossReference(String nameKey) {
        if (!getCrossReferences().isEmpty()) {
            if (getCrossReferences().containsKey(nameKey)) {
                return getCrossReferences().get(nameKey);
            }
        }
        return Collections.emptySet();
    }

    public Set<String> getAdditionalField(String key) {
        if (getAdditional().containsKey(key)) {
            return getAdditional().get(key);
        }
        return Collections.emptySet();
    }

    public void addAdditionalField(String key, String value) {
        Map<String, Set<String>> additional = getAdditional();
        if (key != null && value != null) {
            Set<String> values = new HashSet<>();
            if (additional.containsKey(key)) {
                values = additional.get(key);
            }
            values.add(value);
            additional.put(key, values);
            setAdditional(additional);
        }
    }

    public void setConfigurations(Map<String, String> configurations) {
        this.configurations = configurations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dataset dataset = (Dataset) o;
        return Objects.equals(accession, dataset.accession) &&
                Objects.equals(database, dataset.database) &&
                Objects.equals(name, dataset.name) &&
                Objects.equals(description, dataset.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accession, database, name, description);
    }

    private int initHashCode(){
        int hashCode = 31;
        hashCode  = (accession != null )? 31 * hashCode + accession.hashCode():hashCode;
        hashCode  = (database != null) ?  31 * hashCode + database.hashCode():hashCode;
        hashCode  = (name != null) ? 31 * hashCode + name.hashCode(): hashCode;
        hashCode  = (description != null) ? 31 * hashCode + description.hashCode():hashCode;
        hashCode  = (dates != null) ? 31 * hashCode + dates.hashCode():hashCode;
        hashCode  = (additional!=null)?31 * hashCode + additional.hashCode():hashCode;
        hashCode  = (crossReferences != null)? 31 * hashCode + crossReferences.hashCode():hashCode;
        return hashCode;
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "accession='" + accession + '\'' +
                ", database='" + database + '\'' +
                ", initHashCode=" + initHashCode +
                ", currentStatus='" + currentStatus + '\'' +
                ", isClaimable=" + isClaimable +
                '}';
    }
}
