package uk.ac.ebi.ddi.service.db.service.dataset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetStatus;
import uk.ac.ebi.ddi.service.db.repo.dataset.IDatasetRepoImpl;
import uk.ac.ebi.ddi.service.db.utils.DatasetCategory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yperez on 24/05/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})
public class DatasetStatusServiceTest {

    @Autowired
    private IDatasetStatusService datasetStatusService;

    @Autowired
    private IDatasetRepoImpl iDatasetRepo;

    /*@Test
    public void save() throws Exception {
        Map<String, Set<String>> dates = new HashMap<>();
        Map<String, Set<String>> additional = new HashMap<>();
        Dataset newDataset = new Dataset("ArrayExpress", "E-ATMX-14", "Example Name", "Description Example", dates, additional,additional, DatasetCategory.INSERTED);

        DatasetStatus datasetStatus = new DatasetStatus("E-ATMX-14", "ArrayExpress", newDataset.getInitHashCode(), (new Date()).toString(), DatasetCategory.UPDATED.getType());
        datasetStatusService.save(datasetStatus);
    }*/

    @Test
    public void testSecondaryAccession(){
        iDatasetRepo.getSecondaryAccessions();
    }

}
