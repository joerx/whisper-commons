package io.yodo.whisper.commons.web.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ErrorHandlerSupport {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected ResponseEntity<ErrorResponse> createResponse(Exception e, HttpStatus status) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(e), status);
    }
}
