package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DateField {
      private final String date;

      @JsonCreator
      public DateField(@JsonProperty("$date") String date) {
         this.date = date;
      }

      public String getDate() {
         return date;
      }
   }