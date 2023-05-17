package uk.ac.ebi.ddi.pride.web.service.client.assay;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.pride.web.service.client.assay.AssayWsClient;
import uk.ac.ebi.ddi.pride.web.service.config.AbstractArchiveWsConfig;
import uk.ac.ebi.ddi.pride.web.service.model.assay.AssayDetail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class AssayWsClientTest {

    @Autowired
    AbstractArchiveWsConfig archiveWsConfig;
    AssayWsClient assayWsClient;

    @Before
    public void setUp() throws Exception {
        assayWsClient = new AssayWsClient(archiveWsConfig);
    }

    @Test
    public void testGetAssayByAccession() throws Exception {
        AssayDetail res = assayWsClient.getAssayByAccession("38579");

        assertNotNull(res);
        assertTrue(res.proteinCount > 0);

    }

}