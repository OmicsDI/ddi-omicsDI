package uk.ac.ebi.ddi.service.db.model.similarity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.ddi.service.db.model.logger.AbstractDocument;

import java.io.Serializable;
import java.util.Set;


/**
 * This class control an access to the System using the same headers from a HttpRequest in the web-service
 * The current file control the IP, region, request size, user and date.
 * @author Yasset Perez-Riverol
 */
@CompoundIndexes({
        @CompoundIndex(name = "accession_database", def = "{'accession' : 1, 'database': 1, 'expDataType': 1}", unique = true)
})
@Document(collection = "enrichment.expOutputDataset")
public class ExpOutputDataset extends AbstractDocument implements Serializable{

    private static final long serialVersionUID = 1326887243102331825L;

    //Accession of the dataset
    private String accession;

    //Database of the dataset
    private String database;

    //Type of the dataset
    private String expDataType;

    private Set<String> terms;

    public ExpOutputDataset(String accession, String database, String expDataType, Set<String> terms) {
        this.accession = accession;
        this.database = database;
        this.expDataType = expDataType;
        this.terms = terms;
    }

    public Set<String> getTerms() {
        return terms;
    }

    public void setTerms(Set<String> terms) {
        this.terms = terms;
    }

    public String getExpDataType() {
        return expDataType;
    }

    public void setExpDataType(String expDataType) {
        this.expDataType = expDataType;
    }

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
