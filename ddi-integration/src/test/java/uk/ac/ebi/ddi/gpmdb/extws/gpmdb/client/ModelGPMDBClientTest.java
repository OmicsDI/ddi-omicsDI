package uk.ac.ebi.ddi.gpmdb.extws.gpmdb.client;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ddi.gpmdb.extws.gpmdb.config.GPMDBWsConfigProd;
import uk.ac.ebi.ddi.gpmdb.extws.gpmdb.model.Model;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 02/12/2015
 */
public class ModelGPMDBClientTest {

    ModelGPMDBClient client;

    @Before
    public void setUp() throws Exception {
       client = new ModelGPMDBClient(new GPMDBWsConfigProd());
    }

    @Test
    public void testGetAllProteins() throws Exception {
        String[] proteins = client.getAllProteins("GPM10100159682");

    }

    @Test
    public void testGeModelInformation() throws Exception {
        Model model = client.getModelInformation("GPM32320014494");
    }
}