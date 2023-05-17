package uk.ac.ebi.ddi.service.db.model.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2016
 */
@Document(collection = "datasets.datasetstatus")
public class DatasetStatus{

    String date;

    String status;

    @Indexed
    String accession;

    @Indexed
    String database;

    int hashCode;

    @Id
    ObjectId _id;

    public DatasetStatus() {
    }

    public DatasetStatus(String accession, String database, int hashCode, String date, String status) {
        this.accession = accession;
        this.database  =  database;
        this.date = date;
        this.status = status;
        this.hashCode = hashCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }
}
