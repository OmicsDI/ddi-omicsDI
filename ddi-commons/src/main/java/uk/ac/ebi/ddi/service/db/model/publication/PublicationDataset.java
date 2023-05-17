package uk.ac.ebi.ddi.service.db.model.publication;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.ddi.service.db.model.aggregate.BaseAggregate;

import java.util.Objects;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 04/05/2016
 */
@Document(collection = "publications.publicationdataset")
@CompoundIndexes({
        @CompoundIndex(name = "dataset_publication", def = "{'accession' : 1, 'database': 1, 'pubmedId':1}", unique = true)
})
public class PublicationDataset extends BaseAggregate {

    @Id
    ObjectId _id;

    // Pubmed ID
    String pubmedId;

    // INSERTED ID
    String accession;

    // Database ID
    String database;

    // omicsType
    String omicsType;

    public PublicationDataset() {
    }

    public PublicationDataset(String pubmedId, String datasetID, String databaseID, String omicsType) {
        this.pubmedId = pubmedId;
        this.accession = datasetID;
        this.database = databaseID;
        this.omicsType = omicsType;
    }

    public PublicationDataset(ObjectId _id, String pubmedId, String datasetID, String databaseID, String omicsType) {
        this._id = _id;
        this.pubmedId = pubmedId;
        this.accession = datasetID;
        this.database = databaseID;
        this.omicsType = omicsType;
    }

    public String getPubmedId() {
        return pubmedId;
    }

    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    public String getDatasetID() {
        return accession;
    }

    public void setDatasetID(String datasetID) {
        this.accession = datasetID;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabaseID(String databaseID) {
        this.database = databaseID;
    }

    public String getOmicsType() {
        return omicsType;
    }

    public void setOmicsType(String omicsType) {
        this.omicsType = omicsType;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicationDataset that = (PublicationDataset) o;
        return Objects.equals(pubmedId, that.pubmedId) &&
                Objects.equals(accession, that.accession) &&
                Objects.equals(database, that.database) &&
                Objects.equals(omicsType, that.omicsType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pubmedId, accession, database, omicsType);
    }

    @Override
    public String toString() {
        return "PublicationDataset{" +
                "_id=" + _id +
                ", pubmedId='" + pubmedId + '\'' +
                ", datasetID='" + accession + '\'' +
                ", databaseID='" + database + '\'' +
                ", omicsType='" + omicsType + '\'' +
                '}';
    }
}
