package com.hungrybandits.rest.auth.security.jwt;

import com.hungrybandits.rest.auth.models.entities.User;
import com.hungrybandits.rest.auth.security.UserPrincipal;
import com.hungrybandits.rest.auth.services.UserService;
import com.hungrybandits.rest.exceptions.ApiAccessException;
import com.hungrybandits.rest.exceptions.ApiOperationException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Getter
public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
    private HandlerExceptionResolver exceptionHandler;

    @Autowired
    public void setExceptionHandler( @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        try {

            String token = jwtTokenUtil.extractToken(request);
            if (token == null) {
                chain.doFilter(request, response);
                return;
            }

            jwtTokenUtil.validate(token);

            // Get user identity and set it on the spring security context
            String payload = jwtTokenUtil.getSubjectFromToken(token);
            String email = payload.trim().split(":")[0];
            String profileName = payload.trim().split(":")[1];

            User user = userService.getUserByEmail(email);

            UserDetails userDetails = UserPrincipal.create(user);

            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        catch(ApiOperationException ex){
            exceptionHandler.resolveException(request, response, null,
                    new ApiAccessException(ex.getMessage()));
        } catch (Exception accessException){
            exceptionHandler.resolveException(request, response, null, accessException);
        }
    }
}
