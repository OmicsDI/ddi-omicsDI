package uk.ac.ebi.ddi.service.db.model.logger;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import uk.ac.ebi.ddi.service.db.utils.CategoryType;

import java.io.Serializable;
import java.util.Date;


/**
 * This class control an access to the System using the same headers from a HttpRequest in the web-service
 * The current file control the IP, region, request size, user and date.
 * @author Yasset Perez-Riverol
 */
@Document(collection = "logger.event")
public class HttpEvent extends AbstractEvent implements Serializable{

    private static final long serialVersionUID = 1326887243102331826L;

    String host;

 //   @Indexed(background = true)
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date accessDate;

    String user;

    String path;

    String request;

    String status;

    String response_size;

    String referrer;

    String userAgent;

    /**
     * Default Constructor
     */
    public HttpEvent() {
        this.category = CategoryType.HTTP_EVENT.getCategory();
    }

    /**
     * Constructor with all the information about an access point and the entry.
     * @param host
     * @param accessDate
     * @param user
     * @param path
     * @param request
     * @param status
     * @param response_size
     * @param referrer
     * @param userAgent
     */
    public HttpEvent(String host, Date accessDate, String user, String path, String request, String status, String response_size, String referrer, String userAgent) {
        this.host = host;
        this.accessDate = accessDate;
        this.user = user;
        this.path = path;
        this.request = request;
        this.status = status;
        this.response_size = response_size;
        this.referrer = referrer;
        this.userAgent = userAgent;
        this.category = CategoryType.HTTP_EVENT.getCategory();
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse_size() {
        return response_size;
    }

    public void setResponse_size(String response_size) {
        this.response_size = response_size;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        HttpEvent httpEvent = (HttpEvent) o;

        if (accessDate != null ? !accessDate.equals(httpEvent.accessDate) : httpEvent.accessDate != null) return false;
        if (host != null ? !host.equals(httpEvent.host) : httpEvent.host != null) return false;
        if (path != null ? !path.equals(httpEvent.path) : httpEvent.path != null) return false;
        if (referrer != null ? !referrer.equals(httpEvent.referrer) : httpEvent.referrer != null) return false;
        if (request != null ? !request.equals(httpEvent.request) : httpEvent.request != null) return false;
        if (response_size != null ? !response_size.equals(httpEvent.response_size) : httpEvent.response_size != null)
            return false;
        //noinspection SimplifiableIfStatement
        return status != null ? status.equals(httpEvent.status) : httpEvent.status == null && (user != null ? user.equals(httpEvent.user) : httpEvent.user == null && !(userAgent != null ? !userAgent.equals(httpEvent.userAgent) : httpEvent.userAgent != null));

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (accessDate != null ? accessDate.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (request != null ? request.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (response_size != null ? response_size.hashCode() : 0);
        result = 31 * result + (referrer != null ? referrer.hashCode() : 0);
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpEvent{" +
                "host='" + host + '\'' +
                ", accessDate=" + accessDate +
                ", user='" + user + '\'' +
                ", path='" + path + '\'' +
                ", request='" + request + '\'' +
                ", status='" + status + '\'' +
                ", response_size='" + response_size + '\'' +
                ", referrer='" + referrer + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
