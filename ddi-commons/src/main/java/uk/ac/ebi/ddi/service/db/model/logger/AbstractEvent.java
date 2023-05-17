package uk.ac.ebi.ddi.service.db.model.logger;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 30/04/2015
 */

public abstract class AbstractEvent extends AbstractDocument implements Serializable, IEvent{

    String rawMessage;

    private String logSource;

    private String level;

    private String loggerName;

    private List<String> traceback;

    AbstractResource abstractResource;

    public AbstractEvent(){}

    public AbstractEvent(String rawMessage, String logSource) {
        this.rawMessage = rawMessage;
        this.logSource = logSource;
    }

    @Override
    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public String getLogSource() {
        return logSource;
    }

    public void setLogSource(String logSource) {
        this.logSource = logSource;
    }

    @Override
    public IDocument getResource() {
        return abstractResource;
    }

    public void setResource(AbstractResource abstractResource) {
        this.abstractResource = abstractResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEvent abstractEvent = (AbstractEvent) o;

        return logSource != null ? logSource.equals(abstractEvent.logSource) : abstractEvent.logSource == null && (rawMessage != null ? rawMessage.equals(abstractEvent.rawMessage) : abstractEvent.rawMessage == null && !(abstractResource != null ? !abstractResource.equals(abstractEvent.abstractResource) : abstractEvent.abstractResource != null));

    }

    @Override
    public int hashCode() {
        int result = rawMessage != null ? rawMessage.hashCode() : 0;
        result = 31 * result + (logSource != null ? logSource.hashCode() : 0);
        result = 31 * result + (abstractResource != null ? abstractResource.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "rawMessage='" + rawMessage + '\'' +
                ", logSource='" + logSource + '\'' +
                ", resource=" + abstractResource +
                '}';
    }
}
