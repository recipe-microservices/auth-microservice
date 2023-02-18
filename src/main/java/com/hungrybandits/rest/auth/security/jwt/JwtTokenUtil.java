package com.hungrybandits.rest.auth.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hungrybandits.rest.auth.security.PayloadDetails;
import com.hungrybandits.rest.exceptions.ApiAccessException;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenUtil {

    String extractTokenAndGetSubject(HttpServletRequest request) throws ApiAccessException;

    String extractToken(HttpServletRequest request);

    void validate(String token) throws ApiAccessException, TokenExpiredException;

    String getSubjectFromToken(String token);

    String generateTokenUsingPayload(PayloadDetails payloadDetails) throws ApiAccessException;

    public String extendExpiryTimeForJWT(String token) throws ApiAccessException;

}
