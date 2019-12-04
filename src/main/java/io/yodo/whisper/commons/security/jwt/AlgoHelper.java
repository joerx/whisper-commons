package io.yodo.whisper.commons.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("WeakerAccess")
public class AlgoHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ResourceLoader resourceLoader;

    public AlgoHelper(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Create a HMAC algo for symmetric encryption using a shared secret
     * @param secret shared secret
     * @return HMAC512 algo instance
     */
    public Algorithm makeHMAC(String secret) {
        return Algorithm.HMAC512(secret);
    }

    /**
     * Create an RSA algo instance for asymmetric encryption. This algo requires a private key and can be used to both
     * issue and verify tokens.
     * @param publicKeyPath resource path to the RSA public key file in PKCS#8 format
     * @param privateKeyPath resource path to the RSA private key file in PKCS#8 format
     * @return RSA512 algo instance
     */
    public Algorithm makeRSA(String publicKeyPath, String privateKeyPath) {
        try {
            Resource publicKey = requireNonNull(resourceLoader.getResource(publicKeyPath), "Public key could not be located");
            Resource privateKey = requireNonNull(resourceLoader.getResource(privateKeyPath), "Private key could not be located");

            RSAPublicKey pubKey = readPublicKey(publicKey);
            RSAPrivateKey privKey = readPrivateKey(privateKey);

            return Algorithm.RSA512(pubKey, privKey);
        } catch(Exception e) {
            throw new JWTConfigurationException(e);
        }
    }

    /**
     * Create an RSA algo instance for asymmetric encryption. This algo can only be used to verify tokens.
     * @param publicKeyPath resource path to the RSA public key file in PKCS#8 format
     * @return RSA512 algo instance
     */
    public Algorithm makeRSA(String publicKeyPath) {
        try {
            Resource publicKey = requireNonNull(resourceLoader.getResource(publicKeyPath), "Public key could not be located");
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
