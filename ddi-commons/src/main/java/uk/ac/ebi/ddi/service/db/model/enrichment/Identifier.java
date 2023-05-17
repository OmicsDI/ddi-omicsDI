package uk.ac.ebi.ddi.service.db.model.enrichment;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by azorin on 22/01/2018.
 */
@Document(collection = "identifiers")
public class Identifier {
    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public List<String> getAdditional_accessions() {
        return additional_accessions;
    }

    public void setAdditional_accessions(List<String> additional_accessions) {
        this.additional_accessions = additional_accessions;
    }

    @Id
    String accession;

    @Indexed
    List<String> additional_accessions;
}
