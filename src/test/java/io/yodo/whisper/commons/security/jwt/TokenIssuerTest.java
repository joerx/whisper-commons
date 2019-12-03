package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TokenIssuerTest {

    private static final String SECRET = "s3cret";

    private static final String NAME = "johndoe";

    private static final String ISSUER = "whisper.yodo.io/whisper-test";

    private static final String PRIVATE_KEY_PATH = "classpath:keys/private_key.der";

    private static final String PUBLIC_KEY_PATH = "classpath:keys/public_key.der";

    private ResourceLoader resourceLoader;

    TokenIssuerTest() {
        resourceLoader = new AnnotationConfigApplicationContext("io.yodo.whisper.commons");
    }

    @Test
    void testCanDecodeIssuedTokenUsingHMAC() {
        Algorithm algo = new AlgoHelper().makeHMAC(SECRET);
        TokenIssuer issuer = new TokenIssuer(algo, ISSUER);

        String token = issuer.issueToken(NAME);
        assertNotNull(token);

        DecodedJWT d = JWT.require(algo).withIssuer(ISSUER).build().verify(token);
        assertEquals(NAME, d.getSubject());
        assertEquals(ISSUER, d.getIssuer());
        assertNotNull(d.getExpiresAt());
        assertNotNull(d.getNotBefore());
    }

    @Test
    void testCanLoadPrivateKey() {
        Resource privKey = resourceLoader.getResource(PRIVATE_KEY_PATH);
        assertNotNull(privKey);
    }

    @Test
    void testCanLoadPublicKey() {
        Resource privKey = resourceLoader.getResource(PUBLIC_KEY_PATH);
        assertNotNull(privKey);
    }

    @Test
    void testCanDecodeIssuedTokenUsingRSA() {
        Resource privKey = resourceLoader.getResource(PRIVATE_KEY_PATH);
        Resource pubKey = resourceLoader.getResource(PUBLIC_KEY_PATH);
        Algorithm algo = new AlgoHelper().makeRSA(pubKey, privKey);
        TokenIssuer issuer = new TokenIssuer(algo, ISSUER);

        String token = issuer.issueToken(NAME);
        assertNotNull(token);

        DecodedJWT d = JWT.require(algo).withIssuer(ISSUER).build().verify(token);
        assertEquals(NAME, d.getSubject());
        assertEquals(ISSUER, d.getIssuer());
        assertNotNull(d.getExpiresAt());
        assertNotNull(d.getNotBefore());
    }

    @Test
    void testCanDecodeIssuedTokenUsingRSAWithPublicKeyOnly() {
        Resource privKey = resourceLoader.getResource(PRIVATE_KEY_PATH);
        Resource pubKey = resourceLoader.getResource(PUBLIC_KEY_PATH);
        Algorithm algo = new AlgoHelper().makeRSA(pubKey, privKey);
        TokenIssuer issuer = new TokenIssuer(algo, ISSUER);

        String token = issuer.issueToken(NAME);
        assertNotNull(token);

        Algorithm algo2 = new AlgoHelper().makeRSA(pubKey);
        DecodedJWT d = JWT.require(algo2).withIssuer(ISSUER).build().verify(token);

        assertEquals(NAME, d.getSubject());
        assertEquals(ISSUER, d.getIssuer());
        assertNotNull(d.getExpiresAt());
        assertNotNull(d.getNotBefore());
    }
}
