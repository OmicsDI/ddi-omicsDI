package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Number {
      private final String numberLong;

      @JsonCreator
      public Number(@JsonProperty("$numberLong") String number) {
         this.numberLong = number;
      }

      public String getNumberLong() {
         return numberLong;
      }
   }