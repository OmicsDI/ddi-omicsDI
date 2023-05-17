package uk.ac.ebi.ddi.service.db.model.dataset;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 04/05/2016
 */

@Document(collection = "file.group")
public class FileGroup implements Serializable {

    private String group;

    private List<String> extensions;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileGroup fileGroup = (FileGroup) o;
        return Objects.equals(getGroup(), fileGroup.getGroup()) &&
                Objects.equals(getExtensions(), fileGroup.getExtensions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroup(), getExtensions());
    }
}
