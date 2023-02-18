package com.hungrybandits.rest.auth.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.hungrybandits.rest.auth.config.JWTSecurityProperties;
import com.hungrybandits.rest.auth.enums.Strategy;
import com.hungrybandits.rest.exceptions.ApiAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;

@Component
@EnableConfigurationProperties(JWTSecurityProperties.class)
public class AlgorithmStrategy {

    private final JWTServiceProperties jwtServiceProperties;
    private final KeyGenerator keyGenerator;

    @Autowired
    public AlgorithmStrategy(JWTServiceProperties jwtServiceProperties) {
        this.jwtServiceProperties = jwtServiceProperties;
        this.keyGenerator = new KeyGenerator();
    }

    public Algorithm getAlgorithm(Strategy strategy) throws ApiAccessException {
        Algorithm algorithm = null;
        try {
            switch (strategy) {
                case SYMMETRIC_ENCRYPTION:
                case AUTO:
                    algorithm = Algorithm.HMAC256(jwtServiceProperties.getSecretKey());
                    break;
                case ASYMMETRIC_ENCRYPTION:
                    algorithm = Algorithm.RSA256(keyGenerator.getPublicKeyFromString(jwtServiceProperties.getPublicKey()),
                            keyGenerator.getPrivateKeyFromString(jwtServiceProperties.getPrivateKey()));
                    break;
            }
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new ApiAccessException("Problem with encryption");
        }
        return algorithm;
    }

}
