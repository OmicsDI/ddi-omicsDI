package uk.ac.ebi.ddi.service.db.service.similarity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.model.similarity.ExpOutputDataset;

import java.util.List;

/**
 * Created by mingze on 14/09/15.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})
public class ExpOutputDatasetServiceTest {
    @Autowired
    private IExpOutputDatasetService expOutputDatasetService;

    @Test
    public void testInsert() throws Exception {

    }

    @Test
    public void testRead() throws Exception {

    }

    @Test
    public void testReadAll() throws Exception {
        Page<ExpOutputDataset> datasets = expOutputDatasetService.readAll(0, 10);
        for (ExpOutputDataset dataset : datasets) {
            System.out.println(dataset.getId());
        }
    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {
    }

    @Test
    public void testReadByaccession() throws Exception {

    }

    @Test
    public void testIsDatasetExist() throws Exception {

    }

    @Test
    public void testGetNumberOfDatasets() throws Exception {

    }

    @Test
    public void testDeleteAll() throws Exception {

    }

    @Test
    public void testReadAllInOneType() throws Exception {
        List<ExpOutputDataset> datasets = expOutputDatasetService.readAllInOneType("MetabolomicsData");
        for (ExpOutputDataset dataset : datasets) {
            System.out.println(dataset.getAccession());
        }
    }

}