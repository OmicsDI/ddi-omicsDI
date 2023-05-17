package uk.ac.ebi.ddi.service.db.model.dblucene;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by gaur on 29/3/17.
 */
@Document(collection = "dblucenemapping")
public class DbLuceneMapping {

    @Id
    ObjectId id;

    String dbName;

    String luceneName;

    String displayName;

    String dbUrl;

    String description;

    String logoPath;

    DbLuceneMapping(){

    }

    DbLuceneMapping(String dbName, String luceneName, String displayName){
        this.dbName = dbName;
        this.displayName = displayName;
        this.luceneName = luceneName;
    }

    DbLuceneMapping(String dbName, String luceneName, String displayName, String dbUrl){
        this.dbName = dbName;
        this.displayName = displayName;
        this.luceneName = luceneName;
        this.dbUrl = dbUrl;
    }


    DbLuceneMapping(ObjectId id,String dbName, String luceneName, String displayName){
        this.dbName = dbName;
        this.displayName = displayName;
        this.luceneName = luceneName;
        this.id = id;
    }

    DbLuceneMapping(ObjectId id,String dbName, String luceneName, String displayName,String dbUrl){
        this.dbName = dbName;
        this.displayName = displayName;
        this.luceneName = luceneName;
        this.id = id;
        this.dbUrl = dbUrl;
    }

    DbLuceneMapping(ObjectId id,String dbName, String luceneName, String displayName,String dbUrl,String description,String logoPath){
        this.dbName = dbName;
        this.displayName = displayName;
        this.luceneName = luceneName;
        this.id = id;
        this.dbUrl = dbUrl;
        this.description = description;
        this.logoPath = logoPath;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getLuceneName() {
        return luceneName;
    }

    public void setLuceneName(String luceneName) {
        this.luceneName = luceneName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
