package uk.ac.ebi.ddi.ebe.ws.dao.client.publication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.ebe.ws.dao.client.publication.PublicationWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.QueryResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class PublicationWsClientTest {

    @Autowired
    AbstractEbeyeWsConfig ebeyeWsConfig;

    PublicationWsClient publicationWsClient;

    @Before
    public void setUp() throws Exception {
        publicationWsClient = new PublicationWsClient(ebeyeWsConfig);
    }

    @Test
    public void testGetPublications() throws Exception {
        String[] fields = {"description"};
        String[] ids    = {"23851314","25347964"};
        Set<String> finalIds = new HashSet<String>(Arrays.asList(ids));
        QueryResult pride = publicationWsClient.getPublications(fields, finalIds);
        assertNotNull(pride.getEntries().length == 2);
    }
}