package uk.ac.ebi.ddi.service.db.model.similarity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.ac.ebi.ddi.service.db.model.logger.AbstractDocument;

/**
 * Created by mingze on 07/09/15.
 */
@Document(collection = "enrichment.TermInDB")
@CompoundIndexes({
        @CompoundIndex(name = "accession_database", def = "{'accession' : 1, 'database': 1, 'termAccession': 1}", unique = true)
})
public class TermInDB extends AbstractDocument{

    private String termAccession;

    private String database;

    private String accession;

    private String dataType;

    private boolean IDFCalculated;

    public TermInDB(String accession, String database, String termAccession, String dataType) {
        this.termAccession = termAccession;
        this.dataType      = dataType;
        this.accession     = accession;
        this.database      = database;
        this.IDFCalculated = false;
    }

    public String getTermAccession() {
        return termAccession;
    }

    public void setTermAccession(String termAccession) {
        this.termAccession = termAccession;
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

    public String getTermName() {
        return termAccession;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isIDFCalculated(){
        return IDFCalculated;
    }

    public void setIDFCalculatedAsTrue(){
        this.IDFCalculated = true;
    }
}
