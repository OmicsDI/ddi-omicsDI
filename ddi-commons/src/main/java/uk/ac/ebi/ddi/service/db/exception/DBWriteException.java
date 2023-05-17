package uk.ac.ebi.ddi.service.db.exception;

/**
 * @author Rui Wang, Yasset Perez-Riverol
 * @version $Id$
 */
public class DBWriteException extends RuntimeException {
    public DBWriteException() {
    }

    public DBWriteException(String message) {
        super(message);
    }

    public DBWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBWriteException(Throwable cause) {
        super(cause);
    }

    public DBWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
