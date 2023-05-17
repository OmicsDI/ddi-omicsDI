package uk.ac.ebi.ddi.ebe.ws.dao.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.FieldInfo;
import uk.ac.ebi.ddi.ebe.ws.dao.model.common.IndexInfo;

/**
 * @author ypriverol
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class Domain {

    @JsonProperty("id")
    String id = null;

    @JsonProperty("name")
    String name = null;

    @JsonProperty("description")
    String description = null;

    @JsonProperty("indexInfos")
    IndexInfo[] indexInfo = null;

    @JsonProperty("fieldInfos")
    FieldInfo[] fieldInfos;

    @JsonProperty("subdomains")
    Domain[] subDomains = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IndexInfo[] getIndexInfo() {
        return indexInfo;
    }

    public void setIndexInfo(IndexInfo[] indexInfo) {
        this.indexInfo = indexInfo;
    }

    public FieldInfo[] getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(FieldInfo[] fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public Domain[] getSubDomains() {
        return subDomains;
    }

    public void setSubDomains(Domain[] subDomains) {
        this.subDomains = subDomains;
    }
}
