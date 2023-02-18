package com.hungrybandits.rest.auth.controllers;

import com.hungrybandits.rest.auth.security.jwt.JwtTokenUtil;
import com.hungrybandits.rest.auth.services.UserService;
import com.hungrybandits.rest.auth.services.dtos.entities.UserProxyDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserUpdateDto;
import com.hungrybandits.rest.auth.services.dtos.entities.responses.ApiMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@RestController
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PutMapping("user")
    public ResponseEntity<ApiMessageResponse> update(@RequestBody UserUpdateDto userUpdateDto){
        userService.updateUser(userUpdateDto);
        return ResponseEntity.ok().body(ApiMessageResponse.defaultSuccessResponse());
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<UserProxyDto> getUser(@PathVariable("userId") Long userId){
        UserProxyDto userProxyDto = userService.getUserProxyById(userId);
        return ResponseEntity.ok().body(userProxyDto);
    }

    @GetMapping("user/me")
    public ResponseEntity<UserProxyDto> getUserInSession(HttpServletRequest request) {
        String payload =jwtTokenUtil.extractTokenAndGetSubject(request);
        String email = payload.trim().split(":")[0];
        return ResponseEntity.ok().body(userService.getUserProxyByEmail(email));
    }
}
