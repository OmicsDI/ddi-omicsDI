package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public  class DocSubmission {

      private final IdField             metaId;
      private final String              metaClass;
      private final IdField             id;
      private final String              accNo;
      private final int                 version;
      private final String              owner;
      private final String              submitter;
      private final String              title;
      private final String              method;
      private final String              relPath;
      private final String              rootPath;
      private final boolean             released;
      private final DateField           releaseTime;
      private final DateField           modificationTime;
      private final DateField           creationTime;
      private final DocSection          section;
      private final List<DocAttribute> attributes;
      private final List<String>        tags;
      private final List<DocCollection> collections;
      private final List<DocFile>       pageTabFiles;
      private final String              storageMode;

      @JsonCreator
      public DocSubmission(@JsonProperty("_id") IdField metaId,
                           @JsonProperty("_class") String metaClass,
                           @JsonProperty("id") IdField id,
                           @JsonProperty("accNo") String accNo,
                           @JsonProperty("version") int version,
                           @JsonProperty("owner") String owner,
                           @JsonProperty("submitter") String submitter,
                           @JsonProperty("title") String title,
                           @JsonProperty("method") String method,
                           @JsonProperty("relPath") String relPath,
                           @JsonProperty("rootPath") String rootPath,
                           @JsonProperty("released") boolean released,
                           @JsonProperty("releaseTime") DateField releaseTime,
                           @JsonProperty("modificationTime") DateField modificationTime,
                           @JsonProperty("creationTime") DateField creationTime,
                           @JsonProperty("section") DocSection section,
                           @JsonProperty("attributes") List<DocAttribute> attributes,
                           @JsonProperty("tags") List<String> tags,
                           @JsonProperty("collections") List<DocCollection> collections,
                           @JsonProperty("pageTabFiles") List<DocFile> pageTabFiles,
                           @JsonProperty("storageMode") String storageMode) {
         this.metaId = metaId;
         this.metaClass = metaClass;
         this.id = id;
         this.accNo = accNo;
         this.version = version;
         this.owner = owner;
         this.submitter = submitter;
         this.title = title;
         this.method = method;
         this.relPath = relPath;
         this.rootPath = rootPath;
         this.released = released;
         this.releaseTime = releaseTime;
         this.modificationTime = modificationTime;
         this.creationTime = creationTime;
         this.section = section;
         this.attributes = attributes;
         this.tags = tags;
         this.collections = collections;
         this.pageTabFiles = pageTabFiles;
         this.storageMode = storageMode;
      }

      public IdField getMetaId() {
         return metaId;
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

      public int getVersion() {
         return version;
      }

      public String getOwner() {
         return owner;
      }

      public String getSubmitter() {
         return submitter;
      }

      public String getTitle() {
         return title;
      }

      public String getMethod() {
         return method;
      }

      public String getRelPath() {
         return relPath;
      }

      public String getRootPath() {
         return rootPath;
      }

      public boolean isReleased() {
         return released;
      }

      public DateField getReleaseTime() {
         return releaseTime;
      }

      public DateField getModificationTime() {
         return modificationTime;
      }

      public DateField getCreationTime() {
         return creationTime;
      }

      public DocSection getSection() {
         return section;
      }

      public List<DocAttribute> getAttributes() {
         return attributes;
      }

      public List<String> getTags() {
         return tags;
      }

      public List<DocCollection> getCollections() {
         return collections;
      }

      public List<DocFile> getPageTabFiles() {
         return pageTabFiles;
      }

      public String getStorageMode() {
         return storageMode;
      }
   }