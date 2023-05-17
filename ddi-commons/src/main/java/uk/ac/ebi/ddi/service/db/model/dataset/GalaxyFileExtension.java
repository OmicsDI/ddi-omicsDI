package uk.ac.ebi.ddi.service.db.model.dataset;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 04/05/2016
 */

@Document(collection = "galaxy.extension.mapping")
public class GalaxyFileExtension implements Serializable {

    private String extension;

    private String type;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GalaxyFileExtension that = (GalaxyFileExtension) o;
        return Objects.equals(getExtension(), that.getExtension()) &&
                Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExtension(), getType());
    }
}
