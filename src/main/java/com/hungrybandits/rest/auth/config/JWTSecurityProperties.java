package com.hungrybandits.rest.auth.config;

import com.hungrybandits.rest.auth.security.jwt.JWTServiceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class JWTSecurityProperties {

    @Bean("accessTokenProps")
    @ConfigurationProperties("app.security.jwt.access")
    public JWTServiceProperties getAccessTokenProps() {
        return new JWTServiceProperties();
    }

    @Bean("refreshTokenProps")
    @ConfigurationProperties("app.security.jwt.refresh")
    public JWTServiceProperties getRefreshTokenProps() {
        return new JWTServiceProperties();
    }
}
