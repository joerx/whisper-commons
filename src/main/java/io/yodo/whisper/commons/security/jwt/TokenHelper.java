package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public class TokenHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String secret;

    public TokenHelper(String secret) {
        this.secret = secret;
    }

    Algorithm makeIsserAlgo() {
        try {
            RSAPublicKey pubKey = readPublicKey();
            RSAPrivateKey privKey = readPrivateKey();
            return Algorithm.RSA512(pubKey, privKey);
        } catch(Exception e) {
            throw new JWTConfigurationException(e);
        }
    }

    Algorithm makeVerifierAlgo() {
        try {
            RSAPublicKey pubKey = readPublicKey();
            return Algorithm.RSA512(pubKey, null);
        } catch(Exception e) {
            throw new JWTConfigurationException(e);
        }
    }

    private RSAPrivateKey readPrivateKey() throws Exception {
        URI keyURI = Objects.requireNonNull(getClass().getClassLoader().getResource("/keys/whisper-auth-dev.per")).toURI();
        log.debug("Loading private key from " + keyURI);

        byte[] keyBytes = Files.readAllBytes(Paths.get(keyURI));

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    private RSAPublicKey readPublicKey() throws Exception {
        URI keyURI = Objects.requireNonNull(getClass().getClassLoader().getResource("/keys/whisper-auth-dev-pub.der")).toURI();
        log.debug("Loading public key from " + keyURI);

        byte[] keyBytes = Files.readAllBytes(Paths.get(keyURI));

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) kf.generatePublic(spec);
    }
}
