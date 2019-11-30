package io.yodo.whisper.commons.security.config;

import io.yodo.whisper.commons.security.jwt.TokenDecoder;
import io.yodo.whisper.commons.security.jwt.TokenHelper;
import org.springframework.context.annotation.Bean;

/**
 * Support class for token config. Only provides a method to create the token decoder. For a token issuer, interested
 * parties need to create an instance themselves. This is due to the fact that the issuer might require additional
 * config that is not known to clients that merely want to decode tokens, such as private keys.
 */
@SuppressWarnings("unused")
public abstract class JWTConfigAdapter {

    private String issuer;

    private String secret;

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    protected String getIssuer() {
        return issuer;
    }

    protected String getSecret() {
        return secret;
    }

    @Bean
    public TokenHelper tokenHelper() {
        return new TokenHelper(secret);
    }

    @Bean
    public TokenDecoder tokenDecoder(TokenHelper tokenHelper) {
        return new TokenDecoder(tokenHelper, issuer);
    }
}
