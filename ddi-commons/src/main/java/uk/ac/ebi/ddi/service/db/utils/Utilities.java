package uk.ac.ebi.ddi.service.db.utils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.model.dataset.MostAccessedDatasets;

import java.util.*;

public class Utilities {

    public static Dataset addAdditionalField(Dataset dataset, String key, String value) {
        Map<String, Set<String>> additional = dataset.getAdditional();
        if(additional == null)
            additional = new HashMap<>();
        if(key != null && value != null){
            Set<String> values = new HashSet<>();
            if(additional.containsKey(key))
                values = additional.get(key);
            values.add(value);
            additional.put(key, values);
            dataset.setAdditional(additional);
        }
        return dataset;
    }

    public static BasicDBList getListRawResults(String key, DBObject groupResults){
        List<MostAccessedDatasets> mostAccessedDatasetsList = new ArrayList<>();
        DBObject dbObject = groupResults;
        DBObject data =  (BasicDBObject)dbObject.toMap().get("cursor");
        BasicDBList dbList = (BasicDBList) data.get(key);
        return dbList;
    }

    public static BasicDBList getListRawResultsFromDocument(String key, Document document){
        List<MostAccessedDatasets> mostAccessedDatasetsList = new ArrayList<>();
        BasicDBList dbList = (BasicDBList) document.get(key);
        return dbList;
    }


}
