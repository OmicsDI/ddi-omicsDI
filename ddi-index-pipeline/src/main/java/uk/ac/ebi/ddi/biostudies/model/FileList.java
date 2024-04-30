package uk.ac.ebi.ddi.biostudies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FileList{
    public String _class;
    public String fileName;
    public List<PageTabFile> pageTabFiles;

    @JsonCreator
    public FileList(@JsonProperty("_class") String _class, @JsonProperty("fileName") String fileName,  @JsonProperty("pageTabFiles") List<PageTabFile> pageTabFiles){
        this._class = _class;
        this.fileName = fileName;
        this.pageTabFiles = pageTabFiles;
    }

    public String get_class() {
        return _class;
    }

    public String getFileName() {
        return fileName;
    }

    public List<PageTabFile> getPageTabFiles() {
        return pageTabFiles;
    }
}