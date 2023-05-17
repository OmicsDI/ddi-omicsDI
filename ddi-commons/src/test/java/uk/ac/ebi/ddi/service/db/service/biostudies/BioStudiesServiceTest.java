package uk.ac.ebi.ddi.service.db.service.biostudies;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.service.dataset.IDatasetService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml", "/spring-data-mongo-Test.xml"})
@EnableMongoRepositories
public class BioStudiesServiceTest {

    @Autowired
    private IDatasetService datasetService;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private IBioStudiesParserService bioStudiesParserService;

    @Test
    public void saveStudies() throws Exception {
        bioStudiesParserService.saveStudies();
    }

}
