package uk.ac.ebi.ddi.gpmdb.extws.gpmdb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * 03/12/2015
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Model {

    @JsonProperty("GO subcellular:")
    String subCellular;

    @JsonProperty("project:")
    String projectName;

    @JsonProperty("email:")
    String mail;

    @JsonProperty("institution:")
    String institution;

    @JsonProperty("project comment:")
    String description;

    @JsonProperty("BRENDA cell culture:")
    String cellCulture;

    @JsonProperty("BRENDA tissue:")
    String tissue;

    @JsonProperty("name:")
    String submitter;

    @JsonProperty("CELL cell type:")
    String cellType;

    public String getSubCellular() {
        return subCellular;
    }

    public void setSubCellular(String subCellular) {
        this.subCellular = subCellular;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCellCulture() {
        return cellCulture;
    }

    public void setCellCulture(String cellCulture) {
        this.cellCulture = cellCulture;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    @Override
    public String toString() {
        return "Model{" +
                "subCellular='" + subCellular + '\'' +
                ", projectName='" + projectName + '\'' +
                ", mail='" + mail + '\'' +
                ", institution='" + institution + '\'' +
                ", description='" + description + '\'' +
                ", cellCulture='" + cellCulture + '\'' +
                ", tissue='" + tissue + '\'' +
                ", submitter='" + submitter + '\'' +
                ", cellType='" + cellType + '\'' +
                '}';
    }
}
