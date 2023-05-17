package uk.ac.ebi.ddi.service.db.repo.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.service.db.model.logger.HttpEvent;
import uk.ac.ebi.ddi.service.db.model.logger.ResourceStatVisit;
import uk.ac.ebi.ddi.service.db.utils.CategoryType;

import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2015
 */

@Component
public class IHttpEventRepoImpl {

   @Autowired
    MongoTemplate mongoTemplate;


    public List<ResourceStatVisit> getHttpEventByDatasetResource(int size) {
        Aggregation aggregation = Aggregation.newAggregation(HttpEvent.class,
                Aggregation.match(Criteria.where("abstractResource.category").is(CategoryType.DATASET_RESOURCE.getCategory())),
                Aggregation.group("abstractResource").count().as("total"),
                Aggregation.project("total").and("abstractResource").previousOperation(),
                Aggregation.sort(Sort.Direction.DESC, "total"), Aggregation.limit(size));

        AggregationResults<ResourceStatVisit> groupResults = mongoTemplate.aggregate(aggregation, HttpEvent.class, ResourceStatVisit.class);

        return groupResults.getMappedResults();
    }

}
