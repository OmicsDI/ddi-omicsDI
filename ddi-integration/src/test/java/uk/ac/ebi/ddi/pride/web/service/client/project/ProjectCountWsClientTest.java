package uk.ac.ebi.ddi.pride.web.service.client.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.pride.web.service.client.project.ProjectCountWsClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class ProjectCountWsClientTest {

    @Autowired
    AbstractArchiveWsConfig archiveWsConfig;
    ProjectCountWsClient projectCountWsClient;

    @Before
    public void setUp() throws Exception {
        projectCountWsClient = new ProjectCountWsClient(archiveWsConfig);
    }

    @Test
    public void testGetProjectCount() throws Exception {

        Integer res = projectCountWsClient.getProjectCount("");

        assertNotNull(res);
        assertTrue(res > 0);


    }

    @Test
    public void testGetProjectCountFilters() throws Exception {

        Integer res = projectCountWsClient.getProjectCount("", new String[]{"human", "mouse"},null,null,null,null,null,null,null);
        assertNotNull(res);
        assertTrue(res > 0);

    }

    @Test
    public void testConstructComplexFieldFilter() throws Exception {

    }
}