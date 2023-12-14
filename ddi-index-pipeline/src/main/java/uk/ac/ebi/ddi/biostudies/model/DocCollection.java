package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DocCollection {
      private final String metaClass;
      private final String accNo;

      @JsonCreator
      public DocCollection(@JsonProperty("_class") String metaClass, @JsonProperty("accNo") String accNo) {
         this.metaClass = metaClass;
         this.accNo = accNo;
      }

      public String getMetaClass() {
         return metaClass;
      }

      public String getAccNo() {
         return accNo;
      }
   }