package io.yodo.whisper.commons.web.error;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }
}
