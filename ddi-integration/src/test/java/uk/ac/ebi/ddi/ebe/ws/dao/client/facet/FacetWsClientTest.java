package uk.ac.ebi.ddi.ebe.ws.dao.client.facet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.ebe.ws.dao.client.facet.FacetWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.facet.FacetList;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class FacetWsClientTest {

    @Autowired
    AbstractEbeyeWsConfig ebeyeWsConfig;

    FacetWsClient facetClient;

    @Before
    public void setUp() throws Exception {
       facetClient = new FacetWsClient(ebeyeWsConfig);
    }

    @Test
    public void testGetFacetEntriesByDomains() throws Exception {
        String[] subdomains = {"EGA", "PRIDE", "Metabolights", "PeptideAtlas"};
        FacetList facetList = facetClient.getFacetEntriesByDomains("omics", subdomains, "TAXONOMY", 20);
        assertNotNull(facetList.getFacets().length==1);
    }
}