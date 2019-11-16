package io.yodo.whisper.commons.web.error;

import java.util.Date;

@SuppressWarnings("unused")
public class ErrorResponse {

    private final String message;

    private final String type;

    private final Date timestamp;

    public ErrorResponse(Exception e) {
        this.message = e.getMessage();
        this.type = e.getClass().getName();
        this.timestamp = new Date();
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
