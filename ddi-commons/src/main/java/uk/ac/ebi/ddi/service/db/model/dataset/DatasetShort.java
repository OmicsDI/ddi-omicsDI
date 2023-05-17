package uk.ac.ebi.ddi.service.db.model.dataset;

import java.util.Objects;

/**
 * Created by azorin on 09/02/2018.
 */
public class DatasetShort {


    public DatasetShort(String database, String accession) {
        this.database = database;
        this.accession = accession;
    }

    public DatasetShort() {
    }

    String database;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    String accession;
    String name;
    String sourceUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatasetShort that = (DatasetShort) o;
        return getDatabase().equals(that.getDatabase()) &&
                getAccession().equals(that.getAccession());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDatabase(), getAccession());
    }
}
