package uk.ac.ebi.ddi.pride.web.service.model.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.ddi.pride.web.service.model.common.ContactDetail;

/**
 * @author ypriverol
 * @author jadianes
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProjectDetails {

    @JsonProperty("accession")        // project's accession number,
    public String accession;

    @JsonProperty("submitter")
    public ContactDetail submitter;  // details of the submitter of the dataset

    @JsonProperty("keywords")
    public String keywords;           //relevant keywords associated with the project

    @JsonProperty("doi")
    public String doi;                // the Digital Object Identifier (DOI) for the project (if available)

    @JsonProperty("reanalysis")
    public String reanalysis;         // annotation to indicate that the dataset is a re-analysis based on other public data

    @JsonProperty("experimentTypes")
    public String[] experimentTypes;  // the type(s) of experiment preformed

    @JsonProperty("submissionDate")
    public String submissionDate;     //the date the project has been submitted,

    @JsonProperty("labHeads")
    public ContactDetail[] labHeads;  // the Lab-Head or PI associated to the dataset,

    @JsonProperty("sampleProcessingProtocol")
    public String sampleProcessingProtocol;  //meta-data: information about the sample processing,

    @JsonProperty("dataProcessingProtocol")
    public String dataProcessingProtocol;    // project meta-data: information about the data processing

    @JsonProperty("otherOmicsLink")
    public String  otherOmicsLink;           // links to other datasets related to this project (if available),

    @JsonProperty("quantificationMethods")
    public String[] quantificationMethods;   // the quantification method(s) used with the dataset (if any),

    @JsonProperty("numProteins")
    public int  numProteins;                 // number of reported proteins,

    @JsonProperty("numIdentifiedSpectra")
    public int numIdentifiedSpectra;         // project statistics: number of identified spectra,

    @JsonProperty("numPeptides")             // project statistics: number of reported peptides.
    public int numPeptides;

    @JsonProperty("numUniquePeptides")
    public int numUniquePeptides;            // number of unique peptides

    @JsonProperty("numSpectra")
    public int numSpectra;                   //  number of spectra,

    @JsonProperty("species")
    public String[] species;                 // the species annotation for the project,

    @JsonProperty("tissues")
    public String[] tissues;                 // the tissue annotation for the project,

    @JsonProperty("title")
    public String title;                     // the title given to the project,

    @JsonProperty("publicationDate")
    public String publicationDate;           // the date the project has been made public,

    @JsonProperty("ptmNames")
    public String[] ptmNames;                // the Post Translational Modifications (PTM) annotated for the project,

    @JsonProperty("projectDescription")
    public String projectDescription;        // the description provided for the project

    @JsonProperty("numAssays")
    public int numAssays;                // the number of assays associated with this project,


    @JsonProperty("submissionType")
    public String submissionType;            // the type of submission (complete or partial),

    @JsonProperty("projectTags")
    public String[] projectTags;

    @JsonProperty("instrumentNames")
    public String[] instrumentNames;          //the instrument annotation for the project

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public ContactDetail getSubmitter() {
        return submitter;
    }

    public void setSubmitter(ContactDetail submitter) {
        this.submitter = submitter;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getReanalysis() {
        return reanalysis;
    }

    public void setReanalysis(String reanalysis) {
        this.reanalysis = reanalysis;
    }

    public String[] getExperimentTypes() {
        return experimentTypes;
    }

    public void setExperimentTypes(String[] experimentTypes) {
        this.experimentTypes = experimentTypes;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public ContactDetail[] getLabHeads() {
        return labHeads;
    }

    public void setLabHeads(ContactDetail[] labHeads) {
        this.labHeads = labHeads;
    }

    public String getSampleProcessingProtocol() {
        return sampleProcessingProtocol;
    }

    public void setSampleProcessingProtocol(String sampleProcessingProtocol) {
        this.sampleProcessingProtocol = sampleProcessingProtocol;
    }

    public String getDataProcessingProtocol() {
        return dataProcessingProtocol;
    }

    public void setDataProcessingProtocol(String dataProcessingProtocol) {
        this.dataProcessingProtocol = dataProcessingProtocol;
    }

    public String getOtherOmicsLink() {
        return otherOmicsLink;
    }

    public void setOtherOmicsLink(String otherOmicsLink) {
        this.otherOmicsLink = otherOmicsLink;
    }

    public String[] getQuantificationMethods() {
        return quantificationMethods;
    }

    public void setQuantificationMethods(String[] quantificationMethods) {
        this.quantificationMethods = quantificationMethods;
    }

    public int getNumProteins() {
        return numProteins;
    }

    public void setNumProteins(int numProteins) {
        this.numProteins = numProteins;
    }

    public int getNumIdentifiedSpectra() {
        return numIdentifiedSpectra;
    }

    public void setNumIdentifiedSpectra(int numIdentifiedSpectra) {
        this.numIdentifiedSpectra = numIdentifiedSpectra;
    }

    public int getNumPeptides() {
        return numPeptides;
    }

    public void setNumPeptides(int numPeptides) {
        this.numPeptides = numPeptides;
    }

    public int getNumUniquePeptides() {
        return numUniquePeptides;
    }

    public void setNumUniquePeptides(int numUniquePeptides) {
        this.numUniquePeptides = numUniquePeptides;
    }

    public int getNumSpectra() {
        return numSpectra;
    }

    public void setNumSpectra(int numSpectra) {
        this.numSpectra = numSpectra;
    }

    public String[] getSpecies() {
        return species;
    }

    public void setSpecies(String[] species) {
        this.species = species;
    }

    public String[] getTissues() {
        return tissues;
    }

    public void setTissues(String[] tissues) {
        this.tissues = tissues;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String[] getPtmNames() {
        return ptmNames;
    }

    public void setPtmNames(String[] ptmNames) {
        this.ptmNames = ptmNames;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public int getNumAssays() {
        return numAssays;
    }

    public void setNumAssays(int numAssays) {
        this.numAssays = numAssays;
    }

    public String getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(String submissionType) {
        this.submissionType = submissionType;
    }

    public String[] getProjectTags() {
        return projectTags;
    }

    public void setProjectTags(String[] projectTags) {
        this.projectTags = projectTags;
    }

    public String[] getInstrumentNames() {
        return instrumentNames;
    }

    public void setInstrumentNames(String[] instrumentNames) {
        this.instrumentNames = instrumentNames;
    }
}
