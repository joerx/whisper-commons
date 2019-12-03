package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TokenIssuer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Algorithm algo;

    private final String issuer;

    private final int tokenValidityMinutes;

    public TokenIssuer(Algorithm algo, String issuer) {
        this(algo, issuer, 60);
    }

    public TokenIssuer(Algorithm algo, String issuer, int tokenValidityMinutes) {
        this.algo = algo;
        this.issuer = issuer;
        this.tokenValidityMinutes = tokenValidityMinutes;
    }

    public String issueToken(String name) {
        // calculate expiry date 1 hour from now
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date nbf = cal.getTime();

        cal.add(Calendar.MINUTE, tokenValidityMinutes);
        Date exp = cal.getTime();

        log.debug("Issue token with issuer " + issuer + " valid until " + exp);

        // issue JWT
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(name)
                .withArrayClaim("scope", new String[]{"user", "admin"})
                .withNotBefore(nbf)
                .withExpiresAt(exp)
                .sign(algo);
    }

    public Algorithm getAlgo() {
        return algo;
    }

    public String getIssuer() {
        return issuer;
    }
}
