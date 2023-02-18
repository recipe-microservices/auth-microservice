package com.hungrybandits.rest.auth.security;

import com.hungrybandits.rest.exceptions.ApiAccessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPayloadDetails implements PayloadDetails{
    private String email;
    private String profileName;

    public String getUsername() {
        return email;
    }

    public static UserPayloadDetails createPayloadDetails(UserPrincipal userPrincipal) {
        return new UserPayloadDetails(userPrincipal.getUsername(), userPrincipal.getProfileName());
    }

    @Override
    public String getPayloadAttributesAsString() {
        return getEmail()+":"+getProfileName();
    }

    public static UserPayloadDetails getUserPayloadDetailsFromString(String payloadAsString) {
        String[] attrs = payloadAsString.split(":");
        if (attrs.length < 2) {
            throw new ApiAccessException("The payload used for authentication is invalid");
        }
        return new UserPayloadDetails(attrs[0], attrs[1]);
    }
}
