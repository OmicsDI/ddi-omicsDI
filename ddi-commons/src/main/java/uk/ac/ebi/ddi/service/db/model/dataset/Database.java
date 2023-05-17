package uk.ac.ebi.ddi.service.db.model.dataset;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 05/05/2016
 */
@Document(collection = "datasets.databases")
public class Database {

    @Id
    ObjectId _id;

    @Indexed(unique = true)
    String name;

    String description;

    String releaseDate;

    String releaseTag;

    List<String> omicsType;

    String url;

    public Database() {
    }

    public Database(String name, String description, String releaseDate, String releaseTag, List<String> omicsType, String url) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.releaseTag = releaseTag;
        this.omicsType = omicsType;
        this.url = url;
    }

    public Database(ObjectId _id, String name, String description, String releaseDate, String releaseTag, List<String> omicsType, String url) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.releaseTag = releaseTag;
        this.omicsType = omicsType;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseTag() {
        return releaseTag;
    }

    public void setReleaseTag(String releaseTag) {
        this.releaseTag = releaseTag;
    }

    public List<String> getOmicsType() {
        return omicsType;
    }

    public void setOmicsType(List<String> omicsType) {
        this.omicsType = omicsType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
