package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;

public class TokenHelper {

    private final String secret;

    public TokenHelper(String secret) {
        this.secret = secret;
    }

    Algorithm makeAlgo() {
        return Algorithm.HMAC256(secret);
    }
}
