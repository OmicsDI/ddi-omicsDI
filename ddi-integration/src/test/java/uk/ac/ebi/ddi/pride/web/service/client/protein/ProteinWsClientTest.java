package uk.ac.ebi.ddi.pride.web.service.client.protein;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.pride.web.service.client.protein.ProteinWsClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.protein.ProteinDetailList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class ProteinWsClientTest {

    @Autowired
    AbstractArchiveWsConfig archiveWsConfig;
    ProteinWsClient proteinWsClient;

    @Before
    public void setUp() throws Exception {
        proteinWsClient = new ProteinWsClient(archiveWsConfig);
    }

    @Test
    public void testGetProteinsByProject() throws Exception {
        ProteinDetailList res = proteinWsClient.getProteinsByProject("PXD000402",0,20);
        assertNotNull(res);
        assertEquals(20, res.list.length);
    }

    @Test
    public void testGetProteinsByAssay() throws Exception {
        ProteinDetailList res = proteinWsClient.getProteinsByAssay("38579",0,100);
        assertNotNull(res);
        assertEquals(100, res.list.length);
    }
}