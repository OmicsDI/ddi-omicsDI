package uk.ac.ebi.ddi.service.db.utils;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2015
 */
public class StringToObjectIdConverter implements Converter<String, ObjectId> {

    public ObjectId convert(String source) {
        return new ObjectId(source);
    }

}
