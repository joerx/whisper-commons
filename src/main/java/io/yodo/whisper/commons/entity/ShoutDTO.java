package io.yodo.whisper.commons.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;
import java.util.Objects;

/**
 * DTO class for shouts
 */
@SuppressWarnings("unused")
@JsonInclude(value = Include.NON_EMPTY)
public class ShoutDTO {

    private int id;

    private String message;

    private String username;

    private Date timestamp;

    public ShoutDTO() {
    }

    public ShoutDTO(String message, String username) {
        this.message = message;
        this.username = username;
        this.timestamp = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoutDTO shoutDTO = (ShoutDTO) o;
        return id == shoutDTO.id &&
                message.equals(shoutDTO.message) &&
                username.equals(shoutDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, username);
    }

    @Override
    public String toString() {
        return "ShoutDTO{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
