package com.hungrybandits.rest.auth.services;

import com.hungrybandits.rest.auth.models.entities.Role;
import com.hungrybandits.rest.auth.models.entities.User;
import com.hungrybandits.rest.auth.repositories.RoleRepository;
import com.hungrybandits.rest.auth.repositories.UserRepository;
import com.hungrybandits.rest.auth.services.dtos.entities.UserDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserProxyDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserUpdateDto;
import com.hungrybandits.rest.auth.services.dtos.mappers.UserMapper;
import com.hungrybandits.rest.exceptions.ApiOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProxyDto registerUser(UserDto userDto) {

        Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByEmail(userDto.getEmail()));
        if(optionalUser.isPresent()) {
            throw  new ApiOperationException("User already exists");
        }
        User user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByAuthority("ROLE_USER").orElseThrow(() -> new ApiOperationException("Internal Error"));
        user.setGrantedAuthoritiesList(List.of(role));
        user = userRepository.save(user);
        return userMapper.toUserProxyDto(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ApiOperationException("No such user"));
    }

    public UserProxyDto getUserProxyById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiOperationException("No such user"));
        return userMapper.toUserProxyDto(user);
    }

    public User getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findUserByEmail(email)).orElseThrow(() -> new ApiOperationException("No such user"));
    }

    public UserProxyDto getUserProxyByEmail(String email) {
        User user = Optional.ofNullable(userRepository.findUserByEmail(email)).orElseThrow(() -> new ApiOperationException("No such user"));
        return userMapper.toUserProxyDto(user);
    }

    public void updateUser(UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userUpdateDto.getId()).orElseThrow(() -> new ApiOperationException("No such user"));
        userMapper.toUser(userUpdateDto, user);
    }
}
