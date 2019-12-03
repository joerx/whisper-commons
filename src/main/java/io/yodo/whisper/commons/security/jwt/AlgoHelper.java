package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AlgoHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public AlgoHelper() { }

    /**
     * Create a HMAC algo for symmetric encryption using a shared secret
     * @param secret shared secret
     * @return HMAC512 algo instance
     */
    Algorithm makeHMAC(String secret) {
        return Algorithm.HMAC512(secret);
    }

    /**
     * Create an RSA algo instance for asymmetric encryption. This algo requires a private key and can be used to both
     * issue and verify tokens.
     * @param publicKey file resource representing the RSA public key in PKCS#8 format
     * @param privateKey file resource representing the RSA private key in PKCS#8 format
     * @return RSA512 algo instance
     */
    Algorithm makeRSA(Resource publicKey, Resource privateKey) {
        try {
            RSAPublicKey pubKey = readPublicKey(publicKey);
            RSAPrivateKey privKey = readPrivateKey(privateKey);
            return Algorithm.RSA512(pubKey, privKey);
        } catch(Exception e) {
            throw new JWTConfigurationException(e);
        }
    }

    /**
     * Create an RSA algo instance for asymmetric encryption. This algo can only be used to verify tokens.
     * @param publicKey file resource representing the RSA public key in PKCS#8 format
     * @return RSA512 algo instance
     */
    Algorithm makeRSA(Resource publicKey) {
        try {
            RSAPublicKey pubKey = readPublicKey(publicKey);
            return Algorithm.RSA512(pubKey, null);
        } catch(Exception e) {
            throw new JWTConfigurationException(e);
        }
    }

    private RSAPrivateKey readPrivateKey(Resource privateKey) throws Exception {
        log.debug("Reading private key from " + privateKey.getURI());
        byte[] keyBytes = privateKey.getInputStream().readAllBytes();

        log.debug("Read " + keyBytes.length + " bytes for private key");

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    private RSAPublicKey readPublicKey(Resource publicKey) throws Exception {
        log.debug("Loading public key from " + publicKey.getURI());
        byte[] keyBytes = publicKey.getInputStream().readAllBytes();

        log.debug("Read " + keyBytes.length + " bytes for public key");

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) kf.generatePublic(spec);
    }
}
