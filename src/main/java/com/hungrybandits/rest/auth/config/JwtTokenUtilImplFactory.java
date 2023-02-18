package com.hungrybandits.rest.auth.config;

import com.hungrybandits.rest.auth.security.jwt.JwtTokenUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtTokenUtilImplFactory {
    private final JWTSecurityProperties jwtSecurityProperties;

    @Autowired
    public JwtTokenUtilImplFactory(JWTSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    @Bean("accessTokenUtilImpl")
    public JwtTokenUtilImpl getAccessTokenUtilImpl() {
        return new JwtTokenUtilImpl(jwtSecurityProperties.getAccessTokenProps());
    }

    @Bean("refreshTokenUtilImpl")
    public JwtTokenUtilImpl getRefreshTokenUtilImpl() {
        return new JwtTokenUtilImpl(jwtSecurityProperties.getRefreshTokenProps());
    }
}
