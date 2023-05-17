package uk.ac.ebi.ddi.service.db.service.similarity;

import com.mongodb.BasicDBObjectBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
/**
 * Created by gaur on 27/10/17.
 */
@Service
public class ReanalyzedBiomodels {

    @Autowired
    MongoTemplate mongoTemplate;

    public void reanalyzedModels(){

        AggregationOptions options = Aggregation.newAggregationOptions().allowDiskUse(true).build();

        BasicDBObjectBuilder basicDBObject = new BasicDBObjectBuilder().add("acc","$accession")
        .add("uk/ac/ebi/ddi/service/db","$database");

        Aggregation aggregation = Aggregation.newAggregation(match(Criteria.where("similars.relationType").
                is("Reanalysis of").and("database").is("BioModels Database")),
                unwind("similars"),
                group("similars.similarDataset")
                )
                .withOptions(options);
    }
}
