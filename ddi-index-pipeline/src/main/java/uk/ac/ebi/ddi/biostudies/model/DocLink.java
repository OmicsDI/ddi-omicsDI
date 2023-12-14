package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DocLink {
      private final String             metaClass;
      private final List<DocLink> links;  // In DocLinkTable, etc.
      private final String             url;  // Not in DocLinkTable
      private final List<DocAttribute> attributes;

      public DocLink(@JsonProperty("_class") String metaClass,
                     @JsonProperty("links") List<DocLink> links,
                     @JsonProperty("url") String url,
                     @JsonProperty("attributes") List<DocAttribute> attributes) {
         this.metaClass = metaClass;
         this.links = links;
         this.url = url;
         this.attributes = attributes;
      }

      public String getMetaClass() {
         return metaClass;
      }

      public List<DocLink> getLinks() {
         return links;
      }

      public String getUrl() {
         return url;
      }

      public List<DocAttribute> getAttributes() {
         return attributes;
      }
   }