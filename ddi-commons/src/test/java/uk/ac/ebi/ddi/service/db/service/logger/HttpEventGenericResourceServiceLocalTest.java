package uk.ac.ebi.ddi.service.db.service.logger;


import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ddi.service.db.model.logger.DatasetResource;
import uk.ac.ebi.ddi.service.db.model.logger.HttpEvent;
import uk.ac.ebi.ddi.service.db.utils.Tuple;

import java.util.Date;
import java.util.Map;

/**
 * This class allow to do the testing locally using a mongoDB instance.
 * @author ypriverol
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationTestContext.xml"})

public class HttpEventGenericResourceServiceLocalTest {

    @Autowired
    private DatasetResourceService service;

    @Autowired
    HttpEventService eventService;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void testAddAndGetDatasetAcces() throws Exception {

        DatasetResource datasetResource = new DatasetResource();
        datasetResource.setAccession("PXD0001");
        datasetResource.setDatabase("PRIDE");
        service.save(datasetResource);
        DatasetResource datasetResource1 = service.read(datasetResource.getId());
        Assert.assertEquals(datasetResource.getAccession(), datasetResource1.getAccession());

        //Add a term without id
        datasetResource = new DatasetResource();
        datasetResource.setAccession("PXD0001");
        datasetResource.setDatabase("PRIDE");
        service.save(datasetResource);

        Page<DatasetResource> allEntries = service.readAll(0,20);
        System.out.println(allEntries.getSize());

    }

    @Test
    public void testAddAndGetHttpEvent() throws Exception {

        //Create the resource for the Event
        DatasetResource dataset = new DatasetResource();
        dataset.setAccession("PXD0001");
        dataset.setDatabase("PRIDE");
        dataset.setResourceUUID("http://www.ebi.ac.uk/pride/archive/PXD00001");
        dataset = service.save(dataset);

        //Create an Event
        HttpEvent event = new HttpEvent();
        event.setResource(dataset);
        event.setAccessDate(new Date());
        event.setHost("localhost");
        event.setLogSource("/loganame");
        event.setRawMessage("simple message with the original log message");

        event = eventService.save(event);

        System.out.println(event.toString());
    }

    @Test
    public void readAll(){
        for(int i = 0; i < 100; i++){
            DatasetResource datasetResource = new DatasetResource();
            datasetResource.setAccession("PXD0001" + i);
            datasetResource.setDatabase("PRIDE");
            service.save(datasetResource);
        }
        Page<DatasetResource> datassetAccessList = service.readAll(0, 100);
        for(DatasetResource dat: datassetAccessList){
            System.out.println(dat.toString());
        }
    }

    @Test
    public void read(){
        for(int i = 0; i < 100; i++){
            DatasetResource datasetResource = new DatasetResource();
            datasetResource.setAccession("PXD0001" + i);
            datasetResource.setDatabase("PRIDE");
            datasetResource.setResourceUUID("PXD0001" + i);
            service.save(datasetResource);
        }
        DatasetResource datasetResource = service.read("PXD00013", "PRIDE");
        System.out.println(datasetResource.toString());
    }

    @Test
    public void testAddAccess() throws Exception {
        for(int i = 0; i < 100; i++){
            DatasetResource datasetResource = new DatasetResource();
            datasetResource.setAccession("PXD0001" + i);
            datasetResource.setDatabase("PRIDE");
            service.save(datasetResource);
        }
    }

    @Test
    public void testGetNumberEventByDataset(){
        for(int i = 0; i < 100; i++){
            DatasetResource datasetResource = new DatasetResource();
            datasetResource.setAccession("PXD0001" + i);
            datasetResource.setDatabase("PRIDE");
            datasetResource.setResourceUUID("PXD0001" + i);
            service.save(datasetResource);
        }
        //Create the resource for the Event
        DatasetResource dataset = service.read("PXD00011", "PRIDE");

        for(int i = 0; i < 200; i++){
            //Create an Event
            HttpEvent event = new HttpEvent();
            event.setResource(dataset);
            event.setAccessDate(new Date());
            event.setHost("localhost" + i);
            event.setLogSource("/loganame");
            event.setRawMessage("simple message with the original log message");
            event = eventService.save(event);
            System.out.println(event.toString());
        }

        System.out.println(eventService.getLongEventService(dataset.getAccession(), dataset.getDatabase()));
    }

    @Test
    public void testGetNumberEventByDatasetId(){
        for(int i = 0; i < 100; i++){
            DatasetResource datasetResource = new DatasetResource();
            datasetResource.setAccession("PXD0001" + i);
            datasetResource.setDatabase("PRIDE");
            datasetResource.setResourceUUID("PXD0001" + i);
            service.save(datasetResource);
        }
        //Create the resource for the Event
        DatasetResource dataset = service.read("PXD00011", "PRIDE");

        for(int i = 0; i < 200; i++){
            //Create an Event
            HttpEvent event = new HttpEvent();
            event.setResource(dataset);
            event.setAccessDate(new Date());
            event.setHost("localhost" + i);
            event.setLogSource("/loganame");
            event.setRawMessage("simple message with the original log message");
            event = eventService.save(event);
            System.out.println(event.toString());
        }

        System.out.println(eventService.getEventByResourceId(dataset.getId()));
    }

    @Test
    public void testGetNumberEventByDatasetResource(){
        for(int i = 0; i < 100; i++){
            DatasetResource datasetResource = new DatasetResource();
            datasetResource.setAccession("PXD0001" + i);
            datasetResource.setDatabase("PRIDE");
            datasetResource.setResourceUUID("PXD0001" + i);
            service.save(datasetResource);
        }
        //Create the resource for the Event
        DatasetResource dataset = service.read("PXD00011", "PRIDE");

        DatasetResource dataset1 = service.read("PXD00012", "PRIDE");


        for(int i = 0; i < 200; i++){
            //Create an Event
            HttpEvent event = new HttpEvent();
            if(i%2 ==0)
                event.setResource(dataset);
            else
                event.setResource(dataset1);

            event.setAccessDate(new Date());
            event.setHost("localhost" + i);
            event.setLogSource("/loganame");
            event.setRawMessage("simple message with the original log message");
            event = eventService.save(event);
            System.out.println(event.toString());
        }

        System.out.println(eventService.getHttpEventbyResource(dataset).size());
        Map<Tuple<String, String>, Integer> eventList = eventService.moreAccessedDatasetResource(5);
        System.out.println(eventList.size());

    }

    @Test
    public void testUpdateDatasetAccess() throws Exception {
        for(int i = 0; i < 100; i++){
            DatasetResource datasetResource = new DatasetResource();
            datasetResource.setAccession("PXD0001" + i);
            datasetResource.setDatabase("PRIDE");
            service.save(datasetResource);
        }
        DatasetResource access = service.read("PXD00011", "PRIDE");
        access.setDatabase("PRIDE-2");
        service.update(access);
    }

    @Test
    public void testUserList() throws Exception {
    }

    @Test
    public void testDeleteUser() throws Exception {

    }

}
