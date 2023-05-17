package uk.ac.ebi.ddi.pride.web.service.client.project;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.pride.web.service.client.project.ProjectWsClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.project.ProjectDetails;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class ProjectWsClientTest {

    @Autowired
    AbstractArchiveWsConfig archiveWsConfig;

    ProjectWsClient projectWsClient;

    @Before
    public void setUp() throws Exception {
        projectWsClient = new ProjectWsClient(archiveWsConfig);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetProject() throws Exception {

        ProjectDetails project = projectWsClient.getProject("PXD000320");
        assertNotNull(project);

        assertNotNull(project.submitter);
        assertTrue("The submitter is Matthew", project.submitter.firstName.equalsIgnoreCase("Matthew"));

        assertTrue("Partial submission", project.submissionType.equalsIgnoreCase("PARTIAL"));

        project = projectWsClient.getProject("PXD000402");
        assertNotNull(project);

        assertNotNull(project.submitter);
        assertTrue("The submitter is Matthew", project.submitter.firstName.equalsIgnoreCase("Jean"));

        assertTrue("Partial submission", project.submissionType.equalsIgnoreCase("COMPLETE"));




    }
}