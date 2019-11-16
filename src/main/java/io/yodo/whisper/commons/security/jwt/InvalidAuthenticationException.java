package io.yodo.whisper.commons.security.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("WeakerAccess")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAuthenticationException extends AuthenticationException {
    public InvalidAuthenticationException(String msg) {
        super(msg);
    }

    public InvalidAuthenticationException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
