package uk.ac.ebi.ddi.service.db.model.dataset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * Created by user on 3/12/2017.
 */

@Document(collection = "users")
public class MongoUser {
    @Id
    String userId;

    String userName;

    String accessToken;

    String roles;

    String orcid;

    String bio;

    String homepage;

    String email;

    String affiliation;

    Boolean isPublic;

    Dataset[] dataSets;

    String imageUrl;

    String galaxyInstance;


    byte[] image;

    long expires;

    public UserShort[] getCoauthors() {
        return coauthors;
    }

    public void setCoauthors(UserShort[] coauthors) {
        this.coauthors = coauthors;
    }

    UserShort[] coauthors;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }



    public String getUserId(){
        return userId;
    }
    public void setUserId(String val){
        this.userId = val;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String val){
        this.userName = val;
    }
    public String getAccessToken(){
        return accessToken;
    }
    public void setAccessToken(String val){
        this.accessToken = val;
    }
    public String getRoles(){
        return roles;
    }
    public void setRoles(String val){
        this.roles = val;
    }
    public String getOrcid(){
        return orcid;
    }
    public void setOrcid(String val){
        this.orcid = val;
    }

    public Dataset[] getDataSets() {
        return dataSets;
    }

    public void setDataSets(Dataset[] dataSets) {
        this.dataSets = dataSets;
    }

    public void setImage(byte[] image){
        this.image = image;
    }
    public byte[] getImage(){
        return this.image;
    }

    public String getGalaxyInstance() {
        return galaxyInstance;
    }

    public void setGalaxyInstance(String galaxyInstance) {
        this.galaxyInstance = galaxyInstance;
    }

   
  /*  @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final GrantedAuthority a = new GrantedAuthority(){
           
            public String getAuthority() {
                return "USER";
            }
        };
        return new HashSet<GrantedAuthority>(){{ add(a); }};
    }*/

   
    @JsonIgnore
    public String getPassword() {
        throw new IllegalStateException("password should never be used");
    }

   
    @JsonIgnore
    public String getUsername() {
        return this.getUserName();
    }

   
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

   
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

   
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

   
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
