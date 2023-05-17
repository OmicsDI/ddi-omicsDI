package uk.ac.ebi.ddi.pride.web.service.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ypriverol
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class FileDetail {

    @JsonProperty("fileSource")
    public String fileSource;         // SUBMITTED (part of the original dataset) or GENERATED (added to the submission by PRIDE),

    @JsonProperty("fileType")
    public String fileType;           // ['RESULT' or 'PEAK' or 'SEARCH' or 'RAW' or 'QUANT' or 'GEL' or 'FASTA' or 'SPECTRUM_LIBRARY' or 'MS_IMAGE_DATA' or 'OPTICAL_IMAGE' or 'OTHER']: ,

    @JsonProperty("fileSize")
    public int fileSize;              // size in bytes,

    @JsonProperty("projectAccession")
    public String projectAccession;    // project accession ,

    @JsonProperty("assayAccession")
    public String assayAccession;

    @JsonProperty("downloadLink")
    public String downloadLink;         // public FTP download link,

    @JsonProperty("fileName")
    public String fileName;             // the name of the file

}
