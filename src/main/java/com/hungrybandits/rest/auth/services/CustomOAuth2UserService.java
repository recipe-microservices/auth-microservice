package com.hungrybandits.rest.auth.services;

import com.hungrybandits.rest.auth.enums.AuthProvider;
import com.hungrybandits.rest.auth.models.entities.FullName;
import com.hungrybandits.rest.auth.models.entities.Role;
import com.hungrybandits.rest.auth.models.entities.User;
import com.hungrybandits.rest.auth.repositories.RoleRepository;
import com.hungrybandits.rest.auth.repositories.UserRepository;
import com.hungrybandits.rest.auth.security.UserPrincipal;
import com.hungrybandits.rest.auth.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.hungrybandits.rest.auth.security.oauth2.userinfo.Oauth2UserData;
import com.hungrybandits.rest.exceptions.ApiAccessException;
import com.hungrybandits.rest.exceptions.ApiOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        Oauth2UserData userData = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(userData.getEmail().isBlank()) {
            throw new ApiAccessException("Email not found from OAuth2 provider");
        }
        String email = userData.getEmail();

        User user  = userRepository.findUserByEmail(email);
        if(user != null) {
            if(!user.getAuthProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new ApiAccessException("Looks like you're signed up with " +
                        user.getAuthProvider() + " account. Please use your " + user.getAuthProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, userData);
        } else {
            user = registerNewUser(oAuth2UserRequest, userData);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());

    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, Oauth2UserData oAuth2UserInfo) {
        User user = new User();

        user.setAuthProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setFullName(extractFullName(oAuth2UserInfo.getName()));
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        Role role = roleRepository.findByAuthority("ROLE_USER").orElseThrow(() -> new ApiOperationException("An error occurred"));
        user.setGrantedAuthoritiesList(List.of(role));
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, Oauth2UserData oAuth2UserInfo) {
        String completeName = oAuth2UserInfo.getName();
        if(!completeName.isBlank()) {
            FullName fullName = extractFullName(completeName);
            existingUser.setFullName(fullName);
        }

        if(!oAuth2UserInfo.getImageUrl().isEmpty()) {
            existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        }
        return userRepository.save(existingUser);
    }

    private FullName extractFullName(String name) {
        String firstName = "", middleName = "", lastName="";

        String [] partsOfName = name.split(" ");

        if(partsOfName.length >= 1) {
            if (partsOfName.length == 1) {
                firstName = partsOfName[0];
            } else if (partsOfName.length == 2) {
                firstName = partsOfName[0];
                lastName = partsOfName[1];
            } else {
                firstName = partsOfName[0];
                middleName = partsOfName[1];
                lastName = partsOfName[2];
            }

        }

        return new FullName(firstName, middleName, lastName);
    }

}
