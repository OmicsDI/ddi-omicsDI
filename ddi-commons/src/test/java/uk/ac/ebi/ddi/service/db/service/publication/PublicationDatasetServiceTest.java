package uk.ac.ebi.ddi.service.db.service.publication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.model.publication.PublicationDataset;
import uk.ac.ebi.ddi.service.db.service.dataset.IDatasetService;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 04/05/2016
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})
public class PublicationDatasetServiceTest {

    @Test
    public void save() throws Exception {

    }

    @Autowired
    private IPublicationDatasetService publicationService;

    @Test
    public void testSave() throws Exception {

        PublicationDataset publicationFirst = new PublicationDataset("1","1","1","proteomics");
        publicationService.save(publicationFirst);
        PublicationDataset publicationSecond = new PublicationDataset("1","2","2","proteomics");
        publicationService.save(publicationSecond);
        PublicationDataset publicationThird = new PublicationDataset("2","2","2","proteomics");
        publicationService.save(publicationThird);

    }

    @Test
    public void testFindByPubmedId() throws Exception {

        List<PublicationDataset> datasetList = publicationService.findByPubmedId("1");

    }
}