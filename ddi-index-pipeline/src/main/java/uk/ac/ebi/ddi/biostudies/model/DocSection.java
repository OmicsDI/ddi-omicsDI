package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DocSection {
      private final String             metaClass;
      private final IdField            id;
      private final String             accNo;
      private final String             type;
      private final List<DocAttribute> attributes;
      private final FileList       fileList;
      private final List<DocSection>   sections;
      private final List<DocLink>      links;

      @JsonCreator
      public DocSection(@JsonProperty("_class") String metaClass,
                        @JsonProperty("id") IdField id,
                        @JsonProperty("accNo") String accNo,
                        @JsonProperty("type") String type,
                        @JsonProperty("attributes") List<DocAttribute> attributes,
                        @JsonProperty("fileList") FileList fileList,
                        @JsonProperty("sections") List<DocSection> sections,
                        @JsonProperty("links") List<DocLink> links) {
         this.metaClass = metaClass;
         this.id = id;
         this.accNo = accNo;
         this.type = type;
         this.attributes = attributes;
         this.fileList = fileList;
         this.sections = sections;
         this.links = links;
      }

      public String getMetaClass() {
         return metaClass;
      }

      public IdField getId() {
         return id;
      }

      public String getAccNo() {
         return accNo;
      }

      public String getType() {
         return type;
      }

      public List<DocAttribute> getAttributes() {
         return attributes;
      }

      public FileList getFileList() {
         return fileList;
      }

      public List<DocSection> getSections() {
         return sections;
      }

      public List<DocLink> getLinks() {
         return links;
      }
   }