package com.hungrybandits.rest.auth.services.dtos.mappers;

import com.hungrybandits.rest.auth.models.entities.FullName;
import com.hungrybandits.rest.auth.models.entities.Role;
import com.hungrybandits.rest.auth.models.entities.User;
import com.hungrybandits.rest.auth.services.dtos.entities.RoleDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserProxyDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserUpdateDto;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "dob", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "grantedAuthoritiesList", ignore = true)
    public abstract User toUser(UserProxyDto userProxyDto);

    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "middleName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    public abstract UserProxyDto toUserProxyDto(User user);

    @Mapping(target = "fullName", ignore = true)
    public abstract User toUser(UserDto userDto);

    public abstract List<Role> toRoleList(List<RoleDto> roleDto);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "grantedAuthoritiesList", ignore = true)
    public abstract void toUser(UserUpdateDto userUpdateDto, @MappingTarget User user);

    @AfterMapping
    protected void afterToUserFromUserDto(UserDto userDto ,@MappingTarget User user){
        if(userDto != null){
            FullName fullName = new FullName(userDto.getFirstName(), userDto.getMiddleName(), userDto.getLastName());
            user.setFullName(fullName);
        }
    }

    @AfterMapping
    protected void afterToUserFromUserProxyDto(UserProxyDto userProxyDto, @MappingTarget User user){
        if(userProxyDto != null){
            FullName fullName = new FullName(userProxyDto.getFirstName(),
                    userProxyDto.getMiddleName(), userProxyDto.getLastName());
            user.setFullName(fullName);
        }
    }

    @AfterMapping
    protected void afterToUserProxyDtoFromUser(User user, @MappingTarget UserProxyDto userProxyDto){
        if(user != null){
            userProxyDto.setFirstName(user.getFullName().getFirstName());
            userProxyDto.setMiddleName(user.getFullName().getMiddleName());
            userProxyDto.setLastName(user.getFullName().getLastName());
        }
    }

    @AfterMapping
    protected void afterToUserFromUserUpdateDto(UserUpdateDto userUpdateDto, @MappingTarget User user){
        FullName fullName = new FullName(userUpdateDto.getFirstName(), userUpdateDto.getMiddleName(),
                userUpdateDto.getLastName());
        user.setFullName(fullName);
    }
}
