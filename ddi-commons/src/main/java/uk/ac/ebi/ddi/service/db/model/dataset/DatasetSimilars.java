package uk.ac.ebi.ddi.service.db.model.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yperez on 13/06/2016.
 */
@Document(collection = "datasets.similars")
@CompoundIndexes({
        @CompoundIndex(name = "accession_database", def = "{'accession' : 1, 'database': 1}", unique = true)
})
public class DatasetSimilars implements Serializable{

    @Indexed
    private String accession;
    @Indexed
    private String database;

    @Id
    ObjectId _id;

    private Set<SimilarDataset> similars;

    public DatasetSimilars() {
    }

    public DatasetSimilars(String accession, String database) {
        this.accession = accession;
        this.database = database;
    }

    public DatasetSimilars(String accession, String database, Set<SimilarDataset> similars) {
        this(accession, database);
        this.similars = similars;
    }

    public DatasetSimilars(String accession, String database, SimilarDataset similar) {
        this(accession, database);
        this.similars = new HashSet<>();
        this.similars.add(similar);
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

    public Set<SimilarDataset> getSimilars() {
        return similars;
    }

    public void setSimilars(Set<SimilarDataset> similars) {
        this.similars = similars;
    }

    public ObjectId get_id() {
        return _id;
    }
}
