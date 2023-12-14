package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public  class Attr {
      private final String name;
      private final String value;

      @JsonCreator
      public Attr(@JsonProperty("name") String name, @JsonProperty("value") String value) {
         this.name = name;
         this.value = value;
      }

      public String getName() {
         return name;
      }

      public String getValue() {
         return value;
      }
   }