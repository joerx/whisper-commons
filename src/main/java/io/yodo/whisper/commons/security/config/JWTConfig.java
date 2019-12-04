package io.yodo.whisper.commons.security.config;

import com.auth0.jwt.algorithms.Algorithm;
import io.yodo.whisper.commons.security.jwt.AlgoHelper;
import io.yodo.whisper.commons.security.jwt.TokenDecoder;
import io.yodo.whisper.commons.security.jwt.TokenIssuer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ResourceLoader;

@Configuration
@ConfigurationProperties("security.jwt")
@SuppressWarnings("SpringFacetCodeInspection")
public class JWTConfig {

    private String issuer;

    private String publicKey;

    private String privateKey;

    @Bean
    public AlgoHelper algoHelper(ResourceLoader resourceLoader) {
        return new AlgoHelper(resourceLoader);
    }

    @Lazy
    @Bean
    public TokenDecoder tokenDecoder(AlgoHelper algoHelper) {
        Algorithm algo = algoHelper.makeRSA(publicKey);
        return new TokenDecoder(algo, issuer);
    }

    @Lazy
    @Bean
    public TokenIssuer tokenIssuer(AlgoHelper algoHelper) {
        Algorithm algo = algoHelper.makeRSA(publicKey, privateKey);
        return new TokenIssuer(algo, issuer);
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
