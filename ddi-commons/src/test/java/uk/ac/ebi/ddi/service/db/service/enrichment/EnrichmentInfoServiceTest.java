package uk.ac.ebi.ddi.service.db.service.enrichment;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.model.enrichment.DatasetEnrichmentInfo;
import uk.ac.ebi.ddi.service.db.model.enrichment.WordInField;

import java.util.ArrayList;

/**
 * Created by mingze on 03/08/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})

public class EnrichmentInfoServiceTest {
    @Autowired
    private IEnrichmentInfoService enrichmentService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testAddAndGetDataset() {

        Page<DatasetEnrichmentInfo> allEntries = enrichmentService.readAll(0, 1000000000);


        DatasetEnrichmentInfo dataset = new DatasetEnrichmentInfo("pxd000001", "PRIDE");
        dataset.setStatus("status1");

        ArrayList<WordInField> enrichedTitle = new ArrayList<>();
        enrichedTitle.add(new WordInField("title", 0, 4));
        enrichedTitle.add(new WordInField("title2", 8, 13));
        dataset.addSynonymstoAttribute("title", enrichedTitle);

        ArrayList<WordInField> enrichedAbs = new ArrayList<>();
        enrichedAbs.add(new WordInField("abstr", 0, 4));
        enrichedAbs.add(new WordInField("abstr2", 8, 13));
        dataset.addSynonymstoAttribute("abstract", enrichedAbs);


        enrichmentService.insert(dataset);
        DatasetEnrichmentInfo dataset1 = enrichmentService.read(dataset.getId());
        Assert.assertEquals(dataset.getId(), dataset1.getId());
        Assert.assertEquals(dataset.getAccession(), dataset1.getAccession());

        allEntries = enrichmentService.readAll(0, 100);
        for (DatasetEnrichmentInfo datasettemp : allEntries) {
            //System.out.println(datasettemp.toString());
        }

    }


    @Test
    public void testInsert() throws Exception {

    }

    @Test
    public void testRead() throws Exception {

        DatasetEnrichmentInfo enrichmentInfo = enrichmentService.readByAccession("PXD002287", "PRIDE");

    }


    public void testUpdate() throws Exception {

    }

    public void testDelete() throws Exception {

    }


    public void testIsDatasetExist() throws Exception {

    }
}