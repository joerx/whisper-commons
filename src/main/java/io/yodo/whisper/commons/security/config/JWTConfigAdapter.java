package io.yodo.whisper.commons.security.config;

import com.auth0.jwt.algorithms.Algorithm;
import io.yodo.whisper.commons.security.jwt.TokenDecoder;
import io.yodo.whisper.commons.security.jwt.AlgoHelper;
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
    public AlgoHelper tokenHelper() {
        return new AlgoHelper();
    }

    @Bean
    public TokenDecoder tokenDecoder(Algorithm AlgorithmHelper) {
        return new TokenDecoder(AlgorithmHelper, issuer);
    }
}
