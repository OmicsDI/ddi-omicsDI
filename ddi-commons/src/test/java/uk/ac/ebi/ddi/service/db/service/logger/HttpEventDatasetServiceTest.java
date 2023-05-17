package uk.ac.ebi.ddi.service.db.service.logger;


//import com.github.fakemongo.Fongo;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBCollection;
//import com.mongodb.Mongo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.model.logger.DatasetResource;
//import com.mongodb.DB;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})

public class HttpEventDatasetServiceTest {

//    Fongo fongo = null;
//
//    DB db = null;

    @Autowired
    DatasetResourceService datasetAccessService;

    @Autowired
    HttpEventService httpEventService;

    @Before
    public void setUp(){

//        fongo = new Fongo("mylocaldb");
//
//        db = fongo.getDB("ddiDBTest");
//        DBCollection collection = db.getCollection("datasetaccess");
//
//        collection.insert(new BasicDBObject("name", "jon"));
//        DatasetResource myItem = new DatasetResource("PXD00001", "PRIDE");
//        collection.insert(new BasicDBObject("myItem", myItem));

    }

    @Test
    public void create(){
//        DBCollection collection = db.getCollection("datasetaccess");
//        DatasetResource myItem = new DatasetResource("PXD00002", "PRIDE");
//        collection.insert(new BasicDBObject("myItem", myItem));
    }

    @Test
    public void test_basicOperations() throws Exception {
//        // check if collection is empty
//        Page<DatasetResource> datasetAccesses = datasetAccessService.readAll(0,2);
//        Assert.assertTrue(datasetAccesses.getSize() == 1);
//        // save new document
//        DatasetResource access = new DatasetResource("PXD0003", "PRIDE");
//
//        datasetAccessService.save(access);
//
//        datasetAccesses = datasetAccessService.readAll(0,2);
//
//        // check if document stored
//        Assert.assertEquals(2, datasetAccesses.getSize());
//        // check stored document
//       // Assert.assertEquals(access, datasetAccessService.read(BigInteger.valueOf(3)));
    }

    @Test
    public void test(){
//
//        // get all created databases (they are created automatically the first time requested)
//        Collection<DB> dbs = fongo.getUsedDatabases();
//        for (DB adb : dbs){
//            System.out.println("db: " + adb.toString());
//        }
//        // also
//        List<String> dbNames = fongo.getDatabaseNames();
//        for (String dbname: dbNames){
//            System.out.println("dbname: " + dbname);
//        }
//        // also
////		fongo.dropDatabase("dbName");
//
//        // get an instance of the hijacked com.mongodb.Mongo
//        Mongo mongo = fongo.getMongo();
    }

    @Test
    public void mostAccessed(){
        httpEventService.moreAccessedDataset(20);
    }
}