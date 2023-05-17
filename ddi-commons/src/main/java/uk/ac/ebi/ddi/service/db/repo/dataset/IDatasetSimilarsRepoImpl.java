package uk.ac.ebi.ddi.service.db.repo.dataset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.service.db.model.dataset.DatasetSimilars;

/**
 * Created by azorin on 14/02/2018.
 */

@Component
public class IDatasetSimilarsRepoImpl implements IDatasetSimilarsRepoExtension {


    @Autowired
    MongoOperations mongoOperations;

    //@Query("{'$and':[{accession : ?0}, {database : ?1}]}")
    public DatasetSimilars findByAccessionDatabaseQuery(String acc, String database){
        BasicQuery query = new BasicQuery("{ accession : '"+ acc +"', database : '"+ database +"' }", "{'accession':1,'database':1, 'similars': {$slice: 100}}");

        return mongoOperations.findOne(query, DatasetSimilars.class);
    }
}
