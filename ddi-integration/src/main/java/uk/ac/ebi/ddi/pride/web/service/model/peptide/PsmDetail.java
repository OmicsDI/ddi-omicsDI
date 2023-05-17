package uk.ac.ebi.ddi.pride.web.service.model.peptide;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.ddi.pride.web.service.model.common.ModifiedLocation;

/**
 * PSM details contains the information of different PSMs such as the
 * startPosition, project assay, etc.
 * @user ypriverol
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class PsmDetail {

    @JsonProperty("startPosition")
    public int startPosition;            // the reported position on the protein where the peptide starts

    @JsonProperty("endPosition")
    public int endPosition;              // the reported position on the protein where the peptide ends

    @JsonProperty("projectAccession")
    public String projectAccession;      // the project in which this peptide was reported

    @JsonProperty("assayAccession")
    public String assayAccession;        // the assay in which this peptide was reported,

    @JsonProperty("modifications")
    public ModifiedLocation[] modifications;   // the modifications reported for this peptide

    @JsonProperty("proteinAccession")
    public String proteinAccession;            // the reported accession of the protein containing the peptide

    @JsonProperty("searchEngines")
    public String[] searchEngines;             // the search engine(s) used to generate the identification

    @JsonProperty("searchEngineScores")
    public String[] searchEngineScores;        // the scores reported by the search engine(s)

    @JsonProperty("retentionTime")
    public double retentionTime;               // the reported retention time

    @JsonProperty("charge")
    public int charge;                         // the reported charge

    @JsonProperty("calculatedMZ")
    public double calculatedMZ;                 // the theoretical mz value of the peptide

    @JsonProperty("experimentalMZ")
    public double experimentalMZ;               // the experimental/reported mz value of the peptide

    @JsonProperty("preAA")
    public String preAA;                        // reported amino acid preceding the peptide

    @JsonProperty("postAA")
    public String postAA;                       // reported amino acid following the peptide

    @JsonProperty("spectrumID")
    public String spectrumID;                   // the ID of the spectrum used for the identification

    @JsonProperty("reportedID")
    public String reportedID;                   // the reported ID of this peptide identification (usually, but not necessarily, unique to an assay),

    @JsonProperty("id")
    public String id;                           // a unique ID assigned by PRIDE to this peptide identification,

    @JsonProperty("sequence")
    public String sequence;                     // the peptide sequence identified

}
