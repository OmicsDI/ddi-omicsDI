package uk.ac.ebi.ddi.ebe.ws.dao.client.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.ebe.ws.dao.client.domain.DomainWsClient;
import uk.ac.ebi.ddi.ebe.ws.dao.config.AbstractEbeyeWsConfig;
import uk.ac.ebi.ddi.ebe.ws.dao.model.domain.DomainList;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"/test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)

public class DomainWsClientTest {

    @Autowired
    AbstractEbeyeWsConfig ebeyeWsConfig;

    DomainWsClient domainWsClient;


    @Before
    public void setUp() throws Exception {
        domainWsClient = new DomainWsClient(ebeyeWsConfig);
    }

    @Test
    public void testGetDomainByName() throws Exception {
        DomainList pride = domainWsClient.getDomainByName("omics");
        assertNotNull(pride.list.length==1);
    }


}