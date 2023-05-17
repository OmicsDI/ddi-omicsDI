package uk.ac.ebi.ddi.pride.web.service.client.assay;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.pride.web.service.client.assay.ProjectAssaysWsClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.assay.AssayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class ProjectAssaysWsClientTest {

    @Autowired
    AbstractArchiveWsConfig archiveWsConfig;
    ProjectAssaysWsClient projectAssaysWsClient;

    @Before
    public void setUp() throws Exception {
        projectAssaysWsClient = new ProjectAssaysWsClient(archiveWsConfig);
    }

    @Test
    public void testFindAllByProjectAccession() throws Exception {
        AssayList res = projectAssaysWsClient.findAllByProjectAccession("PXD000402");

        assertNotNull(res);
        assertTrue(res.list.length > 0);
    }
}