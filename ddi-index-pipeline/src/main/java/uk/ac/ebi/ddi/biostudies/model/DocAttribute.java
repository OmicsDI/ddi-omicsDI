package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public  class DocAttribute {
      private final String     metaClass;
      private final String     name;
      private final String     value;
      private final boolean    reference;
      private final List<Attr> nameAttrs;
      private final List<Attr> valueAttrs;

      @JsonCreator
      public DocAttribute(@JsonProperty("_class") String metaClass,
                          @JsonProperty("name") String name,
                          @JsonProperty("value") String value,
                          @JsonProperty("reference") boolean reference,
                          @JsonProperty("nameAttrs") List<Attr> nameAttrs,
                          @JsonProperty("valueAttrs") List<Attr> valueAttrs) {
         this.metaClass = metaClass;
         this.name = name;
         this.value = value;
         this.reference = reference;
         this.nameAttrs = nameAttrs;
         this.valueAttrs = valueAttrs;
      }

      public String getMetaClass() {
         return metaClass;
      }

      public String getName() {
         return name;
      }

      public String getValue() {
         return value;
      }

      public boolean isReference() {
         return reference;
      }

      public List<Attr> getNameAttrs() {
         return nameAttrs;
      }

      public List<Attr> getValueAttrs() {
         return valueAttrs;
      }
   }