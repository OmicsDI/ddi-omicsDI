package uk.ac.ebi.ddi.pride.web.service.model.assay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.ddi.pride.web.service.model.common.ContactDetail;

/**
 * @author ypriverol
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class AssayDetail {

    @JsonProperty("assayAccession")
    public String assayAccession;                // the accession assigned to the assay,

    @JsonProperty("diseases")
    public String[] diseases;                    // disease annotation provided for this assay (if applicable)

    @JsonProperty("proteinCount")
    public int proteinCount;                     // number of proteins in this assay

    @JsonProperty("peptideCount")
    public int peptideCount;                     //number of peptides in this assay

    @JsonProperty("uniquePeptideCount")
    public int uniquePeptideCount;               // number of unique peptides in this assay

    @JsonProperty("identifiedSpectrumCount")
    public int identifiedSpectrumCount;          // number of identified spectra in this assay

    @JsonProperty("totalSpectrumCount")
    public int totalSpectrumCount;               // total number of spectra in this assay,

    @JsonProperty("experimentalFactor")
    public String experimentalFactor;            // experimental factors reported for this assay,

    @JsonProperty("softwares")
    public String[] softwares;                    // software used for the data/result generation,

    @JsonProperty("contacts")
    ContactDetail[] contacts;                     // contact persons, usually the submitter of the dataset,

    @JsonProperty("chromatogram")
    public boolean chromatogram;                  // flag to indicate if a chromatogram is available,

    @JsonProperty("quantMethods")
    public String[] quantMethods;                 // quantification methods used,

    @JsonProperty("ms2Annotation")
    public boolean ms2Annotation;                 // flag if ms level 2 annotation is available

    @JsonProperty("species")
    public String[] species;                      // the species reported for this assay,

    @JsonProperty("title")
    public String title;                          // the title give to the assay by the submitter,

    @JsonProperty("ptmNames")
    public String[] ptmNames;                     // reported modifications

    @JsonProperty("keywords")
    public String keywords;                       // additional keywords added to this assay,

    @JsonProperty("shortLabel")
    public String shortLabel;                     // the short label give to the assay by the submitter,

    @JsonProperty("projectAccession")
    public String projectAccession;               // the project this assay belongs to,

    @JsonProperty("sampleDetails")
    public String[] sampleDetails;                // sample details reported for this assay,

    @JsonProperty("instrumentNames")
    public String[] instrumentNames;              // reported instrument information
}
