package uk.ac.ebi.ddi.ws.modules.error.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.ac.ebi.ddi.ws.modules.error.exception.InvalidDataException;
import uk.ac.ebi.ddi.ws.modules.error.exception.ResourceNotFoundException;
import uk.ac.ebi.pride.web.util.exception.RestError;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

/**
 * @author Jose A. Dianes
 * @author Rui Wang
 * @author Florian Reisinger
 * @author Yasset Perez-Riverol
 *
 */
@ControllerAdvice
public class ExceptionHandlingAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    private RestError handleAccessDeniedException(AccessDeniedException ex, Principal principal) {
        // distinguish two cases:
        //      request for private data, but no user credentials present
        //      user credentials present, but access forbidden for user
        if (principal == null) {
            return new RestError(HttpStatus.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED.value(),
                    "Private data! Please log in.",
                    "Authorisation required.",
                    "http://www.omicsdi.org/login",
                    null);
        } else {
            return new RestError(HttpStatus.FORBIDDEN,
                    HttpStatus.FORBIDDEN.value(),
                    "Access denied for user " + principal.getName(),
                    null);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public RestError handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new RestError(HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                "Not found: " + ex.getMessage(),
                "Repository search returned no result.",
                "If this record should exist, please contact omicsdi-support@ebi.ac.uk",
                null);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    private RestError handleAnyOtherException(Exception ex) {
        LOGGER.error("Unexpected exception", ex);
        return new RestError(HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error: " + ex.getMessage(),
                "Please report to omicsdi-support@ebi.ac.uk",
                null,
                null);
    }



    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidDataException.class)
    @ResponseBody
    private RestError handleIllegalArgumentException(InvalidDataException ex) {
        return new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Invalid data." + ex.getMessage(),
                null);
    }
}
