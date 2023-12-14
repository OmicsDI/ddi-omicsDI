package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public  class IdField {
      private final String oid;

      @JsonCreator
      public IdField(@JsonProperty("$oid") String oid) {
         this.oid = oid;
      }

      public String getOid() {
         return oid;
      }
   }