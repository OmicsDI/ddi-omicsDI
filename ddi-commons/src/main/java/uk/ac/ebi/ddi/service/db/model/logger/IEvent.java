package uk.ac.ebi.ddi.service.db.model.logger;

/**
 * All the methods that should be implemented by the
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 30/04/2015
 */
public interface IEvent {

    /**
     * Every Event should be associated with a Raw Message. This Raw message contains the information
     * of the log registered
     * @return return an string with no format about the logger message.
     */
    String getRawMessage();

    /**
     * The log source should be a path of the log file that produce the log, or google analytics report url,
     * the logSource is a reference to the original source of the message.
     * @return
     */
    String getLogSource();

    /**
     * The resource is a reference to another IDocument that generate this raw message.
     * It can be null if the resource is not annotated in the database.
     * @return IDocument the referenced Document that origin the problem.
     */
    IDocument getResource();


}
