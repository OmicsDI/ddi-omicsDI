package uk.ac.ebi.ddi.service.db.model.feedback;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by gaur on 22/2/17.
 */
@Document(collection ="feedback")
public class Feedback {
    @Id
    ObjectId id;

    Boolean isSatisfied;

    String message;

    String userInfo;

    String searchQuery;

    public Feedback(){

    }

    public Feedback(ObjectId argId, Boolean isSatisfied,String message, String userInfo,String searchQuery){
        this.id = argId;
        this.isSatisfied = isSatisfied;
        this.message = message;
        this.userInfo = userInfo;
        this.searchQuery = searchQuery;
    }

    public Feedback(Boolean isSatisfied,String message, String userInfo,String searchQuery){
        this.isSatisfied = isSatisfied;
        this.message = message;
        this.userInfo = userInfo;
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Boolean getSatisfied() {
        return isSatisfied;
    }

    public void setSatisfied(Boolean satisfied) {
        isSatisfied = satisfied;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }


}
