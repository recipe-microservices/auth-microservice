package com.hungrybandits.rest.auth.security.jwt;

import com.hungrybandits.rest.auth.enums.Strategy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTServiceProperties {
    private String issuer;
    private Strategy strategy;
    private String secretKey;
    private String publicKey;
    private String privateKey;
    private String tokenType;
    private long expiryTimeInMinutes;
}
