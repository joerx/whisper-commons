package io.yodo.whisper.commons.web.error;

import java.util.Date;

@SuppressWarnings("unused")
public class ErrorResponse {

    private String message;

    private String type;

    private Date timestamp;

    public ErrorResponse() {
    }

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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
