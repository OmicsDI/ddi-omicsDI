package uk.ac.ebi.ddi.service.db.exception;

/**
 * @author Rui Wang, Yasset Perez-Riverol
 * @version $Id$
 */
public class DDIReadException extends RuntimeException {

    public DDIReadException() {
    }

    public DDIReadException(String message) {
        super(message);
    }

    public DDIReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DDIReadException(Throwable cause) {
        super(cause);
    }

    public DDIReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
