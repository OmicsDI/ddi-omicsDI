package uk.ac.ebi.ddi.service.db.model.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "datasets.files")
@CompoundIndexes({
        @CompoundIndex(name = "accession_database", def = "{'accession' : 1, 'database': 1}"),
        @CompoundIndex(name = "accession_database_secondary",
                def = "{'accession' : 1, 'database': 1, 'secondary_accession': 1}"),
        @CompoundIndex(name = "accession_database_from", def = "{'accession' : 1, 'database': 1, 'from': 1}"),
})
public class DatasetFile {

    @Id
    private ObjectId _id;

    @Indexed
    private String accession;
    @Indexed
    private String database;

    private String secondaryAccession;


    private String fileUrl;

    @Indexed
    private String from;

    public DatasetFile(String accession, String database, String fileUrl, String from) {
        this.accession = accession;
        this.database = database;
        this.fileUrl = fileUrl;
        this.secondaryAccession = accession;
        this.from = from;
    }

    public DatasetFile(String accession, String database, String secondaryAccession, String fileUrl, String from) {
        this.accession = accession;
        this.database = database;
        this.secondaryAccession = secondaryAccession;
        this.fileUrl = fileUrl;
        this.from = from;
    }

    public DatasetFile() {
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

    public ObjectId getId() {
        return _id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSecondaryAccession() {
        return secondaryAccession;
    }

    public void setSecondaryAccession(String secondaryAccession) {
        this.secondaryAccession = secondaryAccession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatasetFile that = (DatasetFile) o;
        return Objects.equals(getAccession(), that.getAccession()) &&
                Objects.equals(getDatabase(), that.getDatabase()) &&
                Objects.equals(getSecondaryAccession(), that.getSecondaryAccession()) &&
                Objects.equals(getFileUrl(), that.getFileUrl()) &&
                Objects.equals(getFrom(), that.getFrom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccession(), getDatabase(), getSecondaryAccession(), getFileUrl(), getFrom());
    }
}
