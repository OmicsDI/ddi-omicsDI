package uk.ac.ebi.ddi.service.db.service.logger;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.service.db.exception.DBWriteException;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.model.dataset.MostAccessedDatasets;
import uk.ac.ebi.ddi.service.db.model.dataset.Scores;
import uk.ac.ebi.ddi.service.db.model.logger.AbstractResource;
import uk.ac.ebi.ddi.service.db.model.logger.DatasetResource;
import uk.ac.ebi.ddi.service.db.model.logger.HttpEvent;
import uk.ac.ebi.ddi.service.db.model.logger.ResourceStatVisit;
import uk.ac.ebi.ddi.service.db.repo.logger.IDatasetResourceRepo;
import uk.ac.ebi.ddi.service.db.repo.logger.IHttpEventRepo;
import uk.ac.ebi.ddi.service.db.service.database.DatabaseDetailService;
import uk.ac.ebi.ddi.service.db.service.dataset.DatasetService;
import uk.ac.ebi.ddi.service.db.service.dataset.MostAccessedDatasetService;
import uk.ac.ebi.ddi.service.db.utils.Constants;
import uk.ac.ebi.ddi.service.db.utils.Tuple;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 29/04/2015
 */

@Service
public class HttpEventService implements IHttpEventService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private IHttpEventRepo accessRepo;

    @Autowired
    private IDatasetResourceRepo datasetRepo;

    @Autowired
    private DatasetService datasetService;

    @Autowired
    private MostAccessedDatasetService mostAccessedDatasetService;

    @Autowired
    private DatabaseDetailService databaseDetailService;

    @Override
    public HttpEvent insert(HttpEvent httpEvent) {
        if((httpEvent.getResource() != null && httpEvent.getResource().getId() == null))
            throw new DBWriteException(" The reference to the original resource should contain an Id");

        return accessRepo.insert(httpEvent);
    }


    public HttpEvent save(HttpEvent httpEvent) {
        if((httpEvent.getResource() != null && httpEvent.getResource().getId() == null))
            throw new DBWriteException(" The reference to the original resource should contain an Id");
        return accessRepo.save(httpEvent);
    }


    @Override
    public HttpEvent read(ObjectId id) {
        Optional<HttpEvent> httpEvent = accessRepo.findById(id);
        return httpEvent == null ? null : httpEvent.get();
    }

    @Override
    public Page<HttpEvent> readAll(int pageStart, int size) {
        return accessRepo.findAll(PageRequest.of(pageStart, size));
    }

    @Override
    public HttpEvent update(HttpEvent httpEvent) {

        HttpEvent existingHttpEvent = read(httpEvent.getId());

        if (existingHttpEvent == null) {
            throw new RuntimeException("Event not exists " + httpEvent.getId());
        }

        existingHttpEvent.setAccessDate(httpEvent.getAccessDate());
        existingHttpEvent.setHost(httpEvent.getHost());
        existingHttpEvent.setPath(httpEvent.getPath());
        existingHttpEvent.setReferrer(httpEvent.getReferrer());
        existingHttpEvent.setRequest(httpEvent.getRequest());
        existingHttpEvent.setResponse_size(httpEvent.getResponse_size());
        existingHttpEvent.setStatus(httpEvent.getStatus());
        existingHttpEvent.setUser(httpEvent.getUser());
        existingHttpEvent.setUserAgent(httpEvent.getUserAgent());
        existingHttpEvent.setLogSource(httpEvent.getLogSource());
        existingHttpEvent.setRawMessage(httpEvent.getRawMessage());
        existingHttpEvent.setResource((AbstractResource) httpEvent.getResource());

        return save(existingHttpEvent);
    }

    @Override
    public HttpEvent delete(ObjectId id) {
        accessRepo.deleteById(id);
        Optional<HttpEvent>  httpEvent = accessRepo.findById(id);
        return httpEvent == null ? null : httpEvent.get();
    }

    public long getLongEventService( String acccesion, String database){
       return accessRepo.getNumberEventByHttpEventDataSetResource(acccesion, database);
    }

    public List<HttpEvent> getHttpEventbyResource(AbstractResource resource){
        return  accessRepo.findByAbstractResource(resource);
    }

    public long getEventByResourceId(ObjectId _id){
        return accessRepo.getNumberEventByDataResource(_id);
    }

    public Map<Tuple<String, String>, Integer> moreAccessedDatasetResource(int size){
        Map<Tuple<String, String>, Integer> datasets = new HashMap<>();
        List<ResourceStatVisit> currentMostAccessed = accessRepo.getHttpEventByDatasetResource(size);
        //Todo: It would be interesting to to this in batch
        for(ResourceStatVisit visit: currentMostAccessed){
            if(visit.getAbstractResource() != null){
                DatasetResource resource = datasetRepo.findByIdDatabaseQuery(visit.getAbstractResource().getId());
                Tuple<String, String> resourceMap = new Tuple<>(resource.getAccession(), resource.getDatabase());
                datasets.put(resourceMap,visit.getTotal());
            }
        }

        return datasets;
    }

    public void moreAccessedDataset(int size){
        try{
            //DBObject cursor = new BasicDBObject("batchSize",100);
            AggregationOptions options = Aggregation.newAggregationOptions().allowDiskUse(true).cursorBatchSize(100).build();
        //AggregationOptions options = Aggregation.newAggregationOptions().allowDiskUse(true).cursor(cursor).build();

        Aggregation agg = Aggregation.newAggregation(
                //limit(1600),
                group("abstractResource._id").count().as("total")
                        .first("abstractResource.accession").as("accession")
                        .first("abstractResource.database").as("database"),
                project()//.andExpression("_id").as("mostAccessedDatasets._id")
                        .andInclude("total","accession","database"),
                sort(Sort.Direction.DESC, "total")//,
                ,limit(160000)
        ).withOptions(options);

        //try {
        //Convert the aggregation result into a List
        AggregationResults<MostAccessedDatasets> groupResults
                = mongoTemplate.aggregate(agg, Constants.LOGGER_COLLECTION, MostAccessedDatasets.class);

        //groupResults.getRawResults().
       // List<MostAccessedDatasets> currentMostAccessed = groupResults.getMappedResults();
            List<MostAccessedDatasets> currentMostAccessed = getDataFromRawResults(groupResults);
        if(currentMostAccessed.size() > 15) {
            mostAccessedDatasetService.deleteAll();
        }

            for (MostAccessedDatasets visit : currentMostAccessed) {
                if (!visit.getAccession().isEmpty()) {
                    String database = databaseDetailService.retriveAnchorName(visit.getDatabase());
                    Dataset datasetOut = datasetService.read(visit.getAccession(),database);
                    if (datasetOut != null) {

                        if(datasetOut.getScores() != null) {
                            datasetOut.getScores().setViewCount(visit.getTotal());
                        }else{
                            Scores scores = new Scores();
                            scores.setViewCount(visit.getTotal());
                            datasetOut.setScores(scores);
                        }
                        HashSet<String> count = new HashSet<String>();
                        count.add(String.valueOf(visit.getTotal()));
                        datasetOut.getAdditional().put(DSField.Additional.VIEW_COUNT.key(), count);
                        datasetService.update(datasetOut.getId(),datasetOut);
                        MostAccessedDatasets dataset = new MostAccessedDatasets(datasetOut, visit.getTotal());
                        mostAccessedDatasetService.save(dataset);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    public List<MostAccessedDatasets> getDataFromRawResults(AggregationResults groupResults){
        List<MostAccessedDatasets> mostAccessedDatasetsList = new ArrayList<>();
        Document dbObject = groupResults.getRawResults();
        DBObject data =  (BasicDBObject)dbObject.get("cursor");
        BasicDBList dbList = (BasicDBList) data.get("firstBatch");
        mostAccessedDatasetsList = dbList.stream().map(r-> ((HashMap<String, Object>)r)).
                map(r -> new MostAccessedDatasets(new Dataset(r.get("accession").toString(),r.get("database").toString()), (int)r.get("total"))).collect(Collectors.toList());
        return mostAccessedDatasetsList;
    }
}
