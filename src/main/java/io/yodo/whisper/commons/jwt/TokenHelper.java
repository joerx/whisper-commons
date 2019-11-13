package io.yodo.whisper.commons.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.yodo.whisper.commons.InvalidAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TokenHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private String secret;

    private String issuer;

    public Principal verifyToken(String token) {
        return null;
    }

    public String issueToken(Principal principal) {
        // calculate expiry date 1 hour from now
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date nbf = cal.getTime();

        cal.add(Calendar.HOUR, 1);
        Date exp = cal.getTime();

        log.debug("Issue token with issuer " + issuer + " valid until " + exp);

        // issue JWT
        Algorithm algo = makeAlgo();
        return JWT.create()
                .withIssuer(issuer)
                .withClaim("name", principal.getName())
                .withArrayClaim("scope", new String[]{"user", "admin"})
                .withNotBefore(nbf)
                .withExpiresAt(exp)
                .sign(algo);
    }

    private DecodedJWT decodeToken(String token) {
        try {
            Algorithm algo = makeAlgo();
            JWTVerifier ver = JWT.require(algo)
                    .withIssuer(issuer)
                    .build();
            return ver.verify(token);
        } catch (JWTVerificationException ex) {
            throw new InvalidAuthenticationException(ex);
        }
    }

    private Algorithm makeAlgo() {
        return Algorithm.HMAC256(secret);
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}