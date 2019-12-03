package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unused"})
public class TokenDecoder {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Algorithm algo;

    private final String issuer;

    public TokenDecoder(Algorithm algo, String issuer) {
        this.algo = algo;
        this.issuer = issuer;
    }

    public TokenDetails decode(String token) {
        DecodedJWT dec = decodeJWT(token);
        log.debug("Token decoded OK");
        return mapToken(dec);
    }

    private TokenDetails mapToken(DecodedJWT dec) {
        TokenDetails d = new TokenDetails();

        d.setSubject(dec.getSubject());
        d.setScopes(mustMapArray(dec, "scope", String.class));
        d.setExpiresAt(dec.getExpiresAt());
        d.setNotBefore(dec.getNotBefore());
        d.setIssuer(dec.getIssuer());

        return d;
    }

    @SuppressWarnings("SameParameterValue")
    private <T> T[] mustMapArray(DecodedJWT dec, String name, Class<T> t) {
        if (dec.getClaim(name).isNull()) {
            throw new InvalidAuthenticationException("Missing claim '" + name + "'");
        }
        T[] ts = dec.getClaim(name).asArray(t);
        if (ts == null) {
            throw new InvalidAuthenticationException("Invalid format for claim '" + name + "', should be a list");
        }
        return ts;
    }

    private DecodedJWT decodeJWT(String token) {
        try {
            JWTVerifier ver = JWT.require(algo)
                    .withIssuer(issuer)
                    .build();
            return ver.verify(token);
        } catch (JWTVerificationException ex) {
            throw new InvalidAuthenticationException(ex);
        }
    }
}
