package uk.ac.ebi.ddi.pride.web.service.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.pride.web.service.client.assay.ProjectAssaysWsClient;
import uk.ac.ebi.ddi.pride.web.service.client.project.ProjectCountWsClient;
import uk.ac.ebi.ddi.pride.web.service.client.project.ProjectSummaryWsClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.assay.AssayDetail;
import uk.ac.ebi.ddi.pride.web.service.model.assay.AssayList;
import uk.ac.ebi.ddi.pride.web.service.model.projectsummary.ProjectSummary;
import uk.ac.ebi.ddi.pride.web.service.model.projectsummary.ProjectSummaryList;

import java.text.DecimalFormat;

/**
 * Simple example to check all the components. This example retrieve for all the projects
 * PRIDE Archive the assays and for all the assays the difference between number of Spectra and
 * Identified Spectra.
 */

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class SimpleQCstats {

    @Autowired
    AbstractArchiveWsConfig archiveWsConfig;

    ProjectCountWsClient projectCountWsClient;
    ProjectSummaryWsClient projectWsClient;
    ProjectAssaysWsClient projectAssaysWsClient;

    @Before
    public void setUp() throws Exception {
        projectCountWsClient = new ProjectCountWsClient(archiveWsConfig);
        projectWsClient = new ProjectSummaryWsClient(archiveWsConfig);
        projectAssaysWsClient = new ProjectAssaysWsClient(archiveWsConfig);

    }

    @Test
    public void majorAssayWithUnIdentifiedSpectra() throws Exception {

        Integer res = projectCountWsClient.getProjectCount("");

        int i =0;

        double difference = 0;
        String projectID = null;
        String assayID   = null;
        while ( i < res){

            ProjectSummaryList projects = projectWsClient.list("",i,i + 10);
            i = i + 10;

            for(ProjectSummary project: projects.list){
                AssayList assays = projectAssaysWsClient.findAllByProjectAccession(project.accession);
                for(AssayDetail assay: assays.list){
                    double actualDif = assay.totalSpectrumCount - assay.identifiedSpectrumCount;
                    if (actualDif > difference){
                       difference = actualDif;
                       projectID = project.accession;
                       assayID   = assay.assayAccession;
                    }
                }
            }
        }
        System.out.println("The project-assay with the major difference between spectra and identified spectra is: " + projectID + "\t" + assayID);
    }

    @Test
    public void differentIdentifiedAndNoIdentified() throws Exception {

        Integer res = projectCountWsClient.getProjectCount("");

        int i =0;

        double difference = 0;
       int countIdent = 0;
        int countSpectra = 0;
        while ( i < res){

            ProjectSummaryList projects = projectWsClient.list("",i,i + 10);
            i = i + 10;

            for(ProjectSummary project: projects.list){
                AssayList assays = projectAssaysWsClient.findAllByProjectAccession(project.accession);
                for(AssayDetail assay: assays.list){
                    double actualDif = assay.totalSpectrumCount - assay.identifiedSpectrumCount;
                    countIdent = countIdent + assay.identifiedSpectrumCount;
                    countSpectra = countSpectra + assay.totalSpectrumCount;
                    if (actualDif > difference){
                        difference = actualDif;
                    }
                }
            }
        }
        double percentNon = ((double)(countSpectra-countIdent)/(double)countSpectra)*100;
        double percentId  = ((double)countIdent/(double)countSpectra)*100;

        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("The % of unidentifiedSpectra: " + df.format(percentNon) + "\t" + "the % of identified: " + df.format(percentId));
    }

    @Test
    public void getListofProjectwithSetofSpectrainDistribution() throws Exception {

        Integer res = projectCountWsClient.getProjectCount("");

        System.out.println(res);

        int i =0;


        while ( i < res){

            ProjectSummaryList projects = projectWsClient.list("",i,i + 10);
            i = i + 10;

            for(ProjectSummary project: projects.list){
                AssayList assays = projectAssaysWsClient.findAllByProjectAccession(project.accession);
                int countIdent = 0;
                int countSpectra = 0;
                for(AssayDetail assay: assays.list){
                    countSpectra = countSpectra +  assay.totalSpectrumCount;
                    countIdent = countIdent + assay.identifiedSpectrumCount;
                }

                double percentNon = ((double)(countSpectra-countIdent)/(double)countSpectra)*100;
                double percentId  = ((double)countIdent/(double)countSpectra)*100;

                DecimalFormat df = new DecimalFormat("#.##");
                System.out.print("Project: " + project.accession + "\t");
                for(String specie: project.species)
                   System.out.print(specie + "\t");
                System.out.println("The % of unidentifiedSpectra: " + df.format(percentNon) + "\t" + "the % of identified: " + df.format(percentId) + " total: " + countSpectra);

            }
        }
        System.out.println(i);
    }

}
