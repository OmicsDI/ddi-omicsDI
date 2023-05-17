package uk.ac.ebi.ddi.service.db.model.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MergedDataset implements Serializable {

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

        private Map<String, Set<String>> dates;

        // Additional fields
        private Map<String, Set<String>> additional;
        //Cross References
        private Map<String, Set<String>> crossReferences;

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

        public MergedDataset() {
        }

        public MergedDataset(String accession, String database) {
            this.accession = accession;
            this.database = database;
            this.currentStatus = DatasetCategory.INSERTED.getType();
        }

        /**
         * This Constructor generate an Id for the entity using the MongoDB driver
         * @param accession The access of the experiment in the repository
         * @param database  The id of the repository
         */
        public MergedDataset(String accession, String database, DatasetCategory category) {
            this(accession, database);
            this.currentStatus = category.getType();
            this.initHashCode = hashCode();
        }

        public MergedDataset(ObjectId resourceUUID, String accession, String database, DatasetCategory category) {
            this(accession, database);
            this._id = resourceUUID;
            this.currentStatus = category.getType();
            this.initHashCode = hashCode();
        }

        public MergedDataset(Dataset dataset) {
            this(dataset.getAccession(), dataset.getDatabase());
            this.name = dataset.name;
            this.description = dataset.description;
            this.dates = dataset.getDates();
            this.additional = dataset.getAdditional();
            this.crossReferences = dataset.getCrossReferences();
            this.currentStatus = dataset.getCurrentStatus();
            this.initHashCode = dataset.getInitHashCode();
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
            if(additional == null)
                additional = new HashMap<>();
            additional.put(key, values);
        }
        public void addCrossReferences(String key, Set<String> values){
            if(crossReferences == null)
                crossReferences = new HashMap<>();
            crossReferences.put(key, values);
        }



        @Override
        public int hashCode() {
            int hashCode = super.hashCode();
            hashCode  = (accession != null )? 31 * hashCode + accession.hashCode():hashCode;
            hashCode  = (database != null) ?  31 * hashCode + database.hashCode():hashCode;
            hashCode  = (name != null) ? 31 * hashCode + name.hashCode(): hashCode;
            hashCode  = (description != null) ? 31 * hashCode + description.hashCode():hashCode;
            hashCode  = (dates != null) ? 31 * hashCode + dates.hashCode():hashCode;
            hashCode  = (additional!=null)?31 * hashCode + additional.hashCode():hashCode;
            hashCode  = (crossReferences != null)? 31 * hashCode + crossReferences.hashCode():hashCode;

            return hashCode;
        }

        public int initHashCode(){
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
            return "INSERTED{" +
                    "accession='" + accession + '\'' +
                    ", database='" + database + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", dates=" + dates +
                    ", additional=" + additional +
                    ", crossReferences=" + crossReferences +
                    '}';
        }
    }


