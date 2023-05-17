package uk.ac.ebi.ddi.pride.web.service.model.protein;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ypriverol
 */
public class ProteinDetail {

    @JsonProperty("accession")
    public String accession;           // the submitted accession of the identified protein,

    @JsonProperty("projectAccession")
    public String projectAccession;    //the project that identified the protein,

    @JsonProperty("assayAccession")
    public String assayAccession;      // the assay within the project that identified the protein,

    @JsonProperty("synonyms")
    public String[] synonyms;            // accession synonyms; either UniProt accession or EnsEMBL ID,

    @JsonProperty("sequenceType")
    public String sequenceType;        // ['INFERRED' or 'SUBMITTED' or 'NOT_AVAILABLE']: SUBMITTED (provided with dataset) or INFERRED (inferred by PRIDE based on protein accession),

    @JsonProperty("description")
    public String[] description;       //  a description of the protein (usually inferred from protein accession),


    @JsonProperty("sequence")
    public String sequence;            // the AA sequence of the identified protein (may be inferred from protein accession)


}
