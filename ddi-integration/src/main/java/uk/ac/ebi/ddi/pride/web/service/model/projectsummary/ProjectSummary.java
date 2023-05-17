package uk.ac.ebi.ddi.pride.web.service.model.projectsummary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jadianes
 * @author ypriverol
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProjectSummary {

    @JsonProperty("accession")
    public String accession;          // Accession of the project

    @JsonProperty("title")
    public String title;              // the title of th project

    @JsonProperty("species")
    public String[] species;          // The species annotation for the project

    @JsonProperty("tissues")
    public String[] tissues;          //the tissue annotation for the project

    @JsonProperty("publicationDate")
    public String publicationDate;    //the date the project has been made public

    @JsonProperty("ptmNames")
    String[] ptmNames;                //the Post Translational Modifications (PTM) annotated for the project

    @JsonProperty("projectDescription")
    String projectDescription;        //the description provided for the project

    @JsonProperty("numAssays")
    public int numAssays;             //the number of assays associated with this project

    @JsonProperty("submissionType")   // the type of submission (complete or partial)
    public String submissionType;

    @JsonProperty("projectTags")
    public String[] projectTags;      // specific tags added to the project for classification

    @JsonProperty("instrumentNames")
    public String[] instrumentNames;  // the instrument annotation for the project

}
