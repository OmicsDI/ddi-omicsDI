package uk.ac.ebi.ddi.ebe.ws.dao.client.citation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.ebe.ws.dao.client.europmc.CitationClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.europmc.CitationResponse;

import static org.junit.Assert.assertNotNull;

/**
 * Created by gaur on 13/07/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class CitationClientTest {

    @Autowired
    AbstractEbeyeWsConfig ebeyeWsConfig;

    CitationClient citationClient;


    @Before
    public void setUp() throws Exception {
        citationClient = new CitationClient(ebeyeWsConfig);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetDomainByName() throws Exception {
        CitationResponse citationResponse = citationClient.getCitations("E-MTAB-25",1000,"*");
        assertNotNull(citationResponse.count > 0);
    }
}
