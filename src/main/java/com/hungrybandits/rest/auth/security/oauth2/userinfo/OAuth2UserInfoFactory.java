package com.hungrybandits.rest.auth.security.oauth2.userinfo;

import com.hungrybandits.rest.auth.enums.AuthProvider;
import com.hungrybandits.rest.exceptions.ApiAccessException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static Oauth2UserData getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.name())) {
            return new GoogleUserData(attributes);
        }
        else if(registrationId.equalsIgnoreCase(AuthProvider.github.name())) {
            return new GithubUserData(attributes);
        }
        else throw new ApiAccessException("Sorry login with "+ registrationId + " is not supported");
    }
}
