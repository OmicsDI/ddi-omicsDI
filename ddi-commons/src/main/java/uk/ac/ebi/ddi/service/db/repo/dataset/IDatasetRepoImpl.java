package uk.ac.ebi.ddi.service.db.repo.dataset;

import com.mongodb.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.service.db.model.dataset.MergeCandidate;
import uk.ac.ebi.ddi.service.db.utils.Utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by azorin on 11/01/2018.
 */
@Component
public class IDatasetRepoImpl implements IDatasetRepoExtension {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public Set<String> getSecondaryAccessions(){
        //using mongoOperations create the query and execute. Return the property values from document
        //DBObject cursor = new BasicDBObject("batchSize",1000000);
        AggregationOptions options = Aggregation.newAggregationOptions().allowDiskUse(true)
                .cursorBatchSize(1000000).build();

        Query searchQuery = new Query(Criteria.where("additional.additional_accession").exists(true));
        //Long count = mongoOperations.count(searchQuery, Dataset.class);

        Set<String> result = new HashSet<String>();

        //db["datasets.dataset"].aggregate([{$unwind:"$additional.additional_accession"},{$project:{"_id":0,"accession":"$additional.additional_accession"}},{$sort:{"accession":1}}])
       //if(count > )
        Aggregation agg = newAggregation(
                //limit(5000),
                unwind("additional.additional_accession"),
                project().and("additional.additional_accession").as("accession")//,
               // sort(Sort.Direction.DESC, "accession")
        ).withOptions(options);

        AggregationResults<SimpleAccessionObject> results = mongoOperations.aggregate(agg,"datasets.dataset",SimpleAccessionObject.class);

        addtoSet(result, results);
        //CommandResult aggregationOutput = mongoOperations.aggregate(agg,"datasets.dataset",SimpleAccessionObject.class).getRawResults();


        DBObject dbObject = (DBObject) results.getRawResults().get("cursor");
        Long batchId = (Long) dbObject.get("id");
        while(batchId != 0) {
            /*DBObject query = new BasicDBObject();
            query.put("getMore", batchId);
            query.put("collection", "datasets.dataset");*/
            Document query = new Document("getMore", batchId).append("collection", "datasets.dataset");
            Document document = mongoOperations.executeCommand(query);
            BasicDBList dbList = Utilities.getListRawResultsFromDocument("nextBatch",document);
            addtoSet(result,dbList);
            batchId = (Long) document.get("id");
           // batchId = (Long)((DBObject)result1.get("cursor")).get("id");
        }


        //mostAccessedDatasetsList = basicDBList.stream().map(r-> ((HashMap<String, Object>)r)).
        //TODO: upgrade language level return results.getMappedResults().stream().map(x -> x.accession);
/*        Set<String> result = new HashSet<String>();
        for(SimpleAccessionObject o : results.getMappedResults()){
            result.add(o.accession);
        }*/

        System.out.print(String.format("getSecondaryAccessions returns %d entries \n", result.size()));

        return result;
    }

    @Override
    public List<MergeCandidate> getMergeCandidates(int start, int size){

        /*****************************
        db.mergeCandidates.aggregate([
         {$group:{
            "_id":{
                 "database":"$database"
                ,"accession":"$accession"
            },"Similars":{
                $push:
                {
                    "database":"$database1"
                  , "accession":"$accession1"
                }
            }}
         }
         ,{$project:{
                "_id":0
                , "accession":"$_id.accession"
                , "database":"$_id.database"
                , "Similars":"$Similars"
            }
         }
         ,{$sort:{
                "database":1
               ,"accession":1
            }
         }
         ,{$limit:100}
         ,{$skip:10}
         ]).pretty()
        **************************/

        Aggregation agg = newAggregation(
                group("database","accession").push(new BasicDBObject
                        ("accession", "$accession1").append
                        ("database", "$database1")).as("Similars"),
                project().and("_id.accession").as("accession").and("_id.database").as("database").and("Similars").as("Similars"),
                sort(Sort.Direction.DESC, "database").and(Sort.Direction.DESC, "accession"),
                limit(start+size),
                skip(start)
        );

        AggregationResults<MergeCandidate> aggregationResults = mongoOperations.aggregate(agg,"mergeCandidates",MergeCandidate.class);

        List<MergeCandidate> results = aggregationResults.getMappedResults();

        return results;

        /****MergeCandidate candidate = new MergeCandidate();
        candidate.setName("Some name");
        List<MergeCandidate> dummy = new ArrayList<>();
        dummy.add(candidate);return dummy;*******/


    }

    @Override
    public Integer getMergeCandidateCount(){
        return (int) mongoOperations.count(new BasicQuery("{}"),"mergeCandidates");
    }

    @Override
    public void mergeDataset(MergeCandidate mergeData){
        mongoOperations.insert(mergeData,"mergedDatasets");
    }

    @Override
    public void deleteMergeCandidte(String database, String accession){
        mongoOperations.remove(new BasicQuery("{ accession : '"+ accession +"', database : '"+ database +"' }"),"mergeCandidates");

        mongoOperations.remove(new BasicQuery("{ accession1 : '"+ accession +"', database1 : '"+ database +"' }"),"mergeCandidates");
    }


    @Override
    public void delete(String database, String accession){
        mongoOperations.remove(new BasicQuery("{ accession : '"+ accession +"', database : '"+ database +"' }"),"datasets.dataset");
    }

    @Override
    public void addSecondaryAccession(String database, String accession, String secondaryAccession){

        Update update = new Update();

        update.push("additional.secondary_accession",secondaryAccession);

        mongoOperations.updateFirst(new BasicQuery("{ accession : '"+ accession +"', database : '"+ database +"' }"),update, "datasets.dataset");
    }

    @Override
    public long getMergedDatasetCount(String database, String accession){
        return mongoOperations.count(new BasicQuery("{ accession : '"+ accession +"', database : '"+ database +"' }"),"mergeCandidates");
    }

    public void addtoSet(Set<String> result, AggregationResults aggregationResults){
        BasicDBList basicDBList = Utilities.getListRawResultsFromDocument("firstBatch",aggregationResults.getRawResults());
        basicDBList.stream().map(r-> ((HashMap<String, Object>)r))
                .map(r -> result.add(r.get("accession").toString())).collect(Collectors.toList());
    }

    public void addtoSet(Set<String> result, BasicDBList basicDBList){
        basicDBList.stream().map(r-> ((HashMap<String, Object>)r))
                .map(r -> result.add(r.get("accession").toString())).collect(Collectors.toList());
    }
}

