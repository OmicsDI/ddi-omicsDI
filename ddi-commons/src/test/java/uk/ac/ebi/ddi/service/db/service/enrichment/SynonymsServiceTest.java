package uk.ac.ebi.ddi.service.db.service.enrichment;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.model.enrichment.Synonym;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allow to do the testing locally using a mongoDB instance.
 *
 * @author ypriverol
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})

public class SynonymsServiceTest {

    @Autowired
    private ISynonymsService wordService;

//    @Autowired
//    HttpEventService eventService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void testAddAndGetWord() throws Exception {

        Synonym word1 = wordService.insert("testWord1", null);
        Synonym word11 = wordService.read(word1.getId());
        Assert.assertEquals(word1.getId(), word11.getId());
        Assert.assertEquals(word1.getlabel(), word11.getlabel());


        //Add a term without id
        Synonym word2 = wordService.insert("testWord2", null);

        Page<Synonym> allEntries = wordService.readAll(0, 1000000000);
        for (Synonym wordtemp : allEntries) {
            wordService.delete(wordtemp.getId());
        }

        allEntries = wordService.readAll(0, 1000000000);
        for (Synonym wordtemp : allEntries) {
            //System.out.println(wordtemp.toString());
        }

    }

    @Test
    public void readAll() {
        for (int i = 0; i < 100; i++) {
            Synonym word = new Synonym("testWord" + i, null);
//            wordService.insert(word);
        }
//        System.exit(1);
        Page<Synonym> synonymss = wordService.readAll(0, 100000000);
        for (Synonym wordtemp : synonymss) {
//            System.out.println(wordtemp.toString());
        }
    }

    @Test
    public void readAllSynonyms() {

        Page<Synonym> allEntries = wordService.readAll(0, 1000000000);
        for (Synonym wordtemp : allEntries) {
            wordService.delete(wordtemp.getId());
        }

        Synonym word = wordService.insert("testWord", null);
//        for (int i = 0; i < 10; i++) {
//            wordService.insertAsSynonym(word, "testWordSyno" + i);
//        }

        List<String> synonyms = wordService.getAllSynonyms("testWord");
        System.out.println("the synonyms of testWord are: " + synonyms);

        Page<Synonym> synonymss = wordService.readAll(0, 1000);
        for (Synonym wordtemp : synonymss) {
//            System.out.println(wordtemp.toString());
        }
    }


//
//    @Test
//    public void read(){
//        for(int i = 0; i < 100; i++){
//            DatasetResource datasetResource = new DatasetResource();
//            datasetResource.setAccession("PXD0001" + i);
//            datasetResource.setDatabase("PRIDE");
//            datasetResource.setResourceUUID("PXD0001" + i);
//            service.save(datasetResource);
//        }
//        DatasetResource datasetResource = service.read("PXD00013", "PRIDE");
//        System.out.println(datasetResource.toString());
//    }
//
//    @Test
//    public void testAddAccess() throws Exception {
//        for(int i = 0; i < 100; i++){
//            DatasetResource datasetResource = new DatasetResource();
//            datasetResource.setAccession("PXD0001" + i);
//            datasetResource.setDatabase("PRIDE");
//            service.save(datasetResource);
//        }
//    }
//
//    @Test
//    public void testGetNumberEventByDataset(){
//        for(int i = 0; i < 100; i++){
//            DatasetResource datasetResource = new DatasetResource();
//            datasetResource.setAccession("PXD0001" + i);
//            datasetResource.setDatabase("PRIDE");
//            datasetResource.setResourceUUID("PXD0001" + i);
//            service.save(datasetResource);
//        }
//        //Create the resource for the Event
//        DatasetResource dataset = service.read("PXD00011", "PRIDE");
//
//        for(int i = 0; i < 200; i++){
//            //Create an Event
//            HttpEvent event = new HttpEvent();
//            event.setResource(dataset);
//            event.setAccessDate(new Date());
//            event.setHost("localhost" + i);
//            event.setLogSource("/loganame");
//            event.setRawMessage("simple message with the original log message");
//            event = eventService.save(event);
//            System.out.println(event.toString());
//        }
//
//        System.out.println(eventService.getLongEventService(dataset.getAccession(), dataset.getDatabase()));
//    }
//
//    @Test
//    public void testGetNumberEventByDatasetId(){
//        for(int i = 0; i < 100; i++){
//            DatasetResource datasetResource = new DatasetResource();
//            datasetResource.setAccession("PXD0001" + i);
//            datasetResource.setDatabase("PRIDE");
//            datasetResource.setResourceUUID("PXD0001" + i);
//            service.save(datasetResource);
//        }
//        //Create the resource for the Event
//        DatasetResource dataset = service.read("PXD00011", "PRIDE");
//
//        for(int i = 0; i < 200; i++){
//            //Create an Event
//            HttpEvent event = new HttpEvent();
//            event.setResource(dataset);
//            event.setAccessDate(new Date());
//            event.setHost("localhost" + i);
//            event.setLogSource("/loganame");
//            event.setRawMessage("simple message with the original log message");
//            event = eventService.save(event);
//            System.out.println(event.toString());
//        }
//
//        System.out.println(eventService.getEventByResourceId(dataset.getId()));
//    }
//
//    @Test
//    public void testGetNumberEventByDatasetResource(){
//        for(int i = 0; i < 100; i++){
//            DatasetResource datasetResource = new DatasetResource();
//            datasetResource.setAccession("PXD0001" + i);
//            datasetResource.setDatabase("PRIDE");
//            datasetResource.setResourceUUID("PXD0001" + i);
//            service.save(datasetResource);
//        }
//        //Create the resource for the Event
//        DatasetResource dataset = service.read("PXD00011", "PRIDE");
//
//        DatasetResource dataset1 = service.read("PXD00012", "PRIDE");
//
//
//        for(int i = 0; i < 200; i++){
//            //Create an Event
//            HttpEvent event = new HttpEvent();
//            if(i%2 ==0)
//                event.setResource(dataset);
//            else
//                event.setResource(dataset1);
//
//            event.setAccessDate(new Date());
//            event.setHost("localhost" + i);
//            event.setLogSource("/loganame");
//            event.setRawMessage("simple message with the original log message");
//            event = eventService.save(event);
//            System.out.println(event.toString());
//        }
//
//        System.out.println(eventService.getHttpEventbyResource(dataset).size());
//        Map<Tuple<String, String>, Integer> eventList = eventService.moreAccessedDatasetResource(5);
//        System.out.println(eventList.size());
//
//    }
//
//    @Test
//    public void testUpdateDatasetAccess() throws Exception {
//        for(int i = 0; i < 100; i++){
//            DatasetResource datasetResource = new DatasetResource();
//            datasetResource.setAccession("PXD0001" + i);
//            datasetResource.setDatabase("PRIDE");
//            service.save(datasetResource);
//        }
//        DatasetResource access = service.read("PXD00011", "PRIDE");
//        access.setDatabase("PRIDE-2");
//        service.update(access);
//    }
//
//    @Test
//    public void testUserList() throws Exception {
//    }
//
//    @Test
//    public void testDeleteUser() throws Exception {
//
//    }
//
//    @After
//    public void close(){
//        mongoTemplate.getDb().dropDatabase();
//    }

}


