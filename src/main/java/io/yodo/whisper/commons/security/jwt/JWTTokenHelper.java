package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"ConfigurationProperties", "unused"})
@ConfigurationProperties(prefix = "security.jwt")
public class JWTTokenHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private String secret;

    private String issuer;

    private int tokenValidityMinutes = 60;

    User verify(String token) {
        DecodedJWT dec = verifyToken(token);
        log.debug("Token decoded OK");
        return mapClaims(dec);
    }

    private User mapClaims(DecodedJWT dec) {
        if (dec.getClaim("name").isNull()) {
            throw new InvalidAuthenticationException("Missing claim 'name'");
        }

        String username = dec.getClaim("name").asString();
        Collection<GrantedAuthority> authorities = mapAuthorities(dec);
        return new User(username, "", authorities);
    }

    private Collection<GrantedAuthority> mapAuthorities(DecodedJWT dec) {
        if (dec.getClaim("scope").isNull()) {
            throw new InvalidAuthenticationException("Missing claim 'scope'");
        }
        if (dec.getClaim("scope").asList(String.class) == null) {
            throw new InvalidAuthenticationException("Invalid format for claim 'scope', should be a list");
        }

        List<String> scopes = dec.getClaim("scope").asList(String.class);
        return scopes.stream().map(s ->
                new SimpleGrantedAuthority("SCOPE_"+s))
                .collect(Collectors.toSet());
    }

    public String issueToken(String name) {
        // calculate expiry date 1 hour from now
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date nbf = cal.getTime();

        cal.add(Calendar.MINUTE, tokenValidityMinutes);
        Date exp = cal.getTime();

        log.debug("Issue token with issuer " + issuer + " valid until " + exp);

        // issue JWT
        Algorithm algo = makeAlgo();
        return JWT.create()
                .withIssuer(issuer)
                .withClaim("name", name)
                .withArrayClaim("scope", new String[]{"user", "admin"})
                .withNotBefore(nbf)
                .withExpiresAt(exp)
                .sign(algo);
    }

    private DecodedJWT verifyToken(String token) {
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

    public void setTokenValidityMinutes(int tokenValidityMinutes) {
        this.tokenValidityMinutes = tokenValidityMinutes;
    }
}
