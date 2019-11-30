package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TokenIssuer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final TokenHelper tokenHelper;

    private final String issuer;

    private final int tokenValidityMinutes;

    public TokenIssuer(TokenHelper tokenHelper, String issuer) {
        this(tokenHelper, issuer, 60);
    }

    public TokenIssuer(TokenHelper tokenHelper, String issuer, int tokenValidityMinutes) {
        this.tokenHelper = tokenHelper;
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
        Algorithm algo = tokenHelper.makeAlgo();
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(name)
                .withArrayClaim("scope", new String[]{"user", "admin"})
                .withNotBefore(nbf)
                .withExpiresAt(exp)
                .sign(algo);
    }
}
