package uk.ac.ebi.ddi.pride.web.service.client.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.pride.web.service.client.project.ProjectSummaryWsClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.projectsummary.ProjectSummaryList;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class ProjectSummaryWsClientTest {

    @Autowired
    AbstractArchiveWsConfig archiveWsConfig;
    ProjectSummaryWsClient projectWsClient;

    @Before
    public void setUp() throws Exception {
        projectWsClient = new ProjectSummaryWsClient(archiveWsConfig);
    }

    @Test
    public void testList() throws Exception {

        ProjectSummaryList res = projectWsClient.list("",1,10);
        assertNotNull(res);
        assertNotNull(res.list);
        assertTrue(res.list.length > 0);
        assertEquals(10, res.list.length);

    }

    @Test
    public void testListParameters() throws Exception {

        ProjectSummaryList res = projectWsClient.list("",1,10, new String[]{"human", "mouse"},null,null,null,null,null,null,null);
        assertNotNull(res);
        assertNotNull(res.list);
        assertTrue(res.list.length > 0);
    }
}