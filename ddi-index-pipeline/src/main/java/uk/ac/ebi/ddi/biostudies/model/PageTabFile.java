package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PageTabFile{
    private final   String             metaClass;
    private final String             filename;
    private final String             filePath;
    private final String             relPath;
    private final List<DocAttribute> attributes;
    private final String             md5;
    private final Number             fileSize;
    private final String             fullPath;
    private final String             type;

    @JsonCreator
    public PageTabFile(@JsonProperty("_class") String metaClass,
                   @JsonProperty("filename") String filename,
                   @JsonProperty("filePath") String filePath,
                   @JsonProperty("relPath") String relPath,
                   @JsonProperty("attributes") List<DocAttribute> attributes,
                   @JsonProperty("md5") String md5,
                   @JsonProperty("fileSize") Number fileSize,
                   @JsonProperty("fullPath") String fullPath,
                   @JsonProperty("type") String type) {
        this.metaClass = metaClass;
        this.filename = filename;
        this.filePath = filePath;
        this.relPath = relPath;
        this.attributes = attributes;
        this.md5 = md5;
        this.fileSize = fileSize;
        this.fullPath = fullPath;
        this.type = type;
    }

    public String getMetaClass() {
        return metaClass;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getRelPath() {
        return relPath;
    }

    public List<DocAttribute> getAttributes() {
        return attributes;
    }

    public String getMd5() {
        return md5;
    }

    public Number getFileSize() {
        return fileSize;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getType() {
        return type;
    }
}