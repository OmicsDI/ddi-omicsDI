package uk.ac.ebi.ddi.service.db.service.similarity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.model.similarity.TermInDB;
import uk.ac.ebi.ddi.service.db.service.similarity.ITermInDBService;

/**
 * This class allow to do the testing locally using a mongoDB instance.
 *
 * @author  Mingze
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})

public class TermInDBServiceTest {

    @Autowired
    private ITermInDBService termService;

//    @Autowired
//    HttpEventService eventService;

    @Autowired
    private MongoTemplate mongoTemplate;



    @Test
    public void readAll() {
//        for (int i = 0; i < 100; i++) {
//            TermInDB term = new TermInDB("testTerm"+i,"testType");
//            System.out.println(term.getId());
//            termService.insert(term);
//        }
////        System.exit(1);
//        Page<TermInDB> terms = termService.readAll(0, 100000000);
//        for (TermInDB term : terms) {
//            System.out.println(term.toString());
//        }
    }





}


