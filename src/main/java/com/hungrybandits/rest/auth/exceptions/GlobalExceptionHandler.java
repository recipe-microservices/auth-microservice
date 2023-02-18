package com.hungrybandits.rest.auth.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hungrybandits.rest.exceptions.CommonExceptionHandler;
import com.hungrybandits.rest.exceptions.dtos.ApiCallError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends CommonExceptionHandler {
    private final Logger logger = LogManager.getLogger();

    public GlobalExceptionHandler() {
    }
    @ExceptionHandler({TokenExpiredException.class})
    public ResponseEntity<ApiCallError> handleAuthenticationException(HttpServletRequest request, TokenExpiredException ex) {
        this.logger.error("handleTokenExpiredException {}\n", request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiCallError(HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value(), request.getRequestURI(), "TOKEN_EXPIRED", LocalDateTime.now(), List.of(ex.getMessage())));
    }
}
