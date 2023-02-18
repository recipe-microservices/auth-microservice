package com.hungrybandits.rest.auth.security;

import com.hungrybandits.rest.auth.models.entities.User;
import com.hungrybandits.rest.auth.services.dtos.entities.UserProxyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private UserProxyDto user;
    private String jwtToken;
    private String refreshToken;

    public static AuthResponse mapUserToAuthResponse(User user, String jwtToken, String refreshToken) {
        return new AuthResponse(new UserProxyDto(user.getId(), user.getFullName().getFirstName(), user.getFullName().getMiddleName(), user.getFullName().getLastName(), user.getProfileName(), user.getEmail(), user.getUserSummary(), user.getDob()), jwtToken, refreshToken);
    }
}
