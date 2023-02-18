package com.hungrybandits.rest.auth.controllers;

import com.hungrybandits.rest.auth.security.AuthRequest;
import com.hungrybandits.rest.auth.security.AuthResponse;
import com.hungrybandits.rest.auth.services.CustomUserDetailsService;
import com.hungrybandits.rest.auth.services.UserService;
import com.hungrybandits.rest.auth.services.dtos.entities.UserDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserProxyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;

    @Autowired
    public AuthController(CustomUserDetailsService customUserDetailsService, UserService userService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }

    @PostMapping("public/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest authRequest){
        AuthResponse authResponse = customUserDetailsService.login(authRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(authResponse);
    }

    @GetMapping("public/getToken")
    public ResponseEntity<AuthResponse> getToken(HttpServletRequest request){
        AuthResponse authResponse = customUserDetailsService.getToken(request);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(authResponse);
    }

    @PostMapping("public/register")
    public ResponseEntity<UserProxyDto> register(@RequestBody UserDto userDto){
        UserProxyDto userProxyDto = userService.registerUser(userDto);
        return ResponseEntity.ok().body(userProxyDto);
    }
}
