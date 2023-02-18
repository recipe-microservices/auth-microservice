package com.hungrybandits.rest.auth.services;

import com.hungrybandits.rest.auth.models.entities.User;
import com.hungrybandits.rest.auth.repositories.UserRepository;
import com.hungrybandits.rest.auth.security.*;
import com.hungrybandits.rest.auth.security.jwt.JwtTokenUtil;
import com.hungrybandits.rest.exceptions.ApiAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepos;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil accessTokenUtil;
    private final JwtTokenUtil refreshTokenUtil;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepos, AuthenticationManager authenticationManager,
                                    @Qualifier("accessTokenUtilImpl") JwtTokenUtil accessTokenUtil,
                                    @Qualifier("refreshTokenUtilImpl") JwtTokenUtil refreshTokenUtil) {
        this.userRepos = userRepos;
        this.authenticationManager = authenticationManager;
        this.accessTokenUtil = accessTokenUtil;
        this.refreshTokenUtil = refreshTokenUtil;
    }

    public AuthResponse login(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return AuthResponse.mapUserToAuthResponse(userPrincipal.getUser(), accessTokenUtil
                    .generateTokenUsingPayload(new UserPayloadDetails(userPrincipal.getUsername(), userPrincipal.getProfileName())), refreshTokenUtil
                    .generateTokenUsingPayload(() -> userPrincipal.getUsername().concat(":").concat(userPrincipal.getProfileName())
                            .concat(":").concat(LocalDateTime.now().toString())));

        } catch (BadCredentialsException badCredentialsException) {
            throw new ApiAccessException(badCredentialsException.getLocalizedMessage());
        }
    }

    public AuthResponse getToken(HttpServletRequest request) {
        String refreshToken = refreshTokenUtil.extractToken(request);
        refreshTokenUtil.validate(refreshToken);
        String refreshToken2 = refreshTokenUtil.extendExpiryTimeForJWT(refreshToken);
        return new AuthResponse(null,
                accessTokenUtil.generateTokenUsingPayload(
                        UserPayloadDetails.getUserPayloadDetailsFromString(refreshTokenUtil.getSubjectFromToken(refreshToken2))
                ), refreshToken2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepos.findUserByEmail(username)).orElseThrow(() -> new UsernameNotFoundException("Incorrect credentials"));
        return UserPrincipal.create(user);
    }
}
