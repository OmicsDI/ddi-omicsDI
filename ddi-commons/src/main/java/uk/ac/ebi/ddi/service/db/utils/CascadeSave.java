package uk.ac.ebi.ddi.service.db.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 29/04/2015
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface CascadeSave {

}
