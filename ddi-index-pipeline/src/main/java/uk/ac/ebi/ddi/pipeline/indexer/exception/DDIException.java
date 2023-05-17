package uk.ac.ebi.ddi.pipeline.indexer.exception;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/08/2015
 */
public class DDIException extends Exception {

    public DDIException() {
    }

    public DDIException(String message) {
        super(message);
    }

    public DDIException(String message, Throwable cause) {
        super("The config file was removed or was not provided please check the folder config/config.xml", cause);
    }

    public DDIException(Throwable cause) {
        super(cause);
    }

    public DDIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
