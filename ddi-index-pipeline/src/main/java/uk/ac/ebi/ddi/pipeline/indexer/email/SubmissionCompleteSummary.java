package uk.ac.ebi.ddi.pipeline.indexer.email;


import lombok.Getter;

/**
 * Summary of the submissions details, including PRIDE Inspector URL etc
 *
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/08/2015
 */
@Getter
public class SubmissionCompleteSummary {
    private final String projectAccession;
    private final String doi;
    private final String projectName;
    private final String submitterName;
    private final String reviewerUserName;
    private final String reviewerPassword;
    private final String bioSamplesId;

    public SubmissionCompleteSummary(String projectAccession,
                                     String doi,
                                     String projectName,
                                     String submitterName,
                                     String reviewerUserName,
                                     String reviewerPassword,
                                     String bioSamplesId) {
        this.projectAccession = projectAccession;
        this.doi = doi;
        this.projectName = projectName;
        this.submitterName = submitterName;
        this.reviewerUserName = reviewerUserName;
        this.reviewerPassword = reviewerPassword;
        this.bioSamplesId = bioSamplesId;
    }
}
