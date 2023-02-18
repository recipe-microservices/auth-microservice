package com.hungrybandits.rest.auth.services.dtos.mappers;

import com.hungrybandits.rest.auth.models.entities.Role;
import com.hungrybandits.rest.auth.models.entities.User;
import com.hungrybandits.rest.auth.services.dtos.entities.RoleDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserProxyDto;
import com.hungrybandits.rest.auth.services.dtos.entities.UserUpdateDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-18T23:20:53+0530",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.33.0.v20221215-1352, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public User toUser(UserProxyDto userProxyDto) {
        if ( userProxyDto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userProxyDto.getEmail() );
        user.setId( userProxyDto.getId() );
        user.setProfileName( userProxyDto.getProfileName() );
        user.setUserSummary( userProxyDto.getUserSummary() );

        afterToUserFromUserProxyDto( userProxyDto, user );

        return user;
    }

    @Override
    public UserProxyDto toUserProxyDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserProxyDto userProxyDto = new UserProxyDto();

        userProxyDto.setEmail( user.getEmail() );
        userProxyDto.setId( user.getId() );
        userProxyDto.setProfileName( user.getProfileName() );
        userProxyDto.setUserSummary( user.getUserSummary() );
        userProxyDto.setDob( user.getDob() );

        afterToUserProxyDtoFromUser( user, userProxyDto );

        return userProxyDto;
    }

    @Override
    public User toUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userDto.getEmail() );
        user.setGrantedAuthoritiesList( toRoleList( userDto.getGrantedAuthoritiesList() ) );
        user.setId( userDto.getId() );
        user.setPassword( userDto.getPassword() );
        user.setProfileName( userDto.getProfileName() );
        user.setUserSummary( userDto.getUserSummary() );
        user.setDob( userDto.getDob() );

        afterToUserFromUserDto( userDto, user );

        return user;
    }

    @Override
    public List<Role> toRoleList(List<RoleDto> roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        List<Role> list = new ArrayList<Role>( roleDto.size() );
        for ( RoleDto roleDto1 : roleDto ) {
            list.add( roleDtoToRole( roleDto1 ) );
        }

        return list;
    }

    @Override
    public void toUser(UserUpdateDto userUpdateDto, User user) {
        if ( userUpdateDto == null ) {
            return;
        }

        if ( userUpdateDto.getEmail() != null ) {
            user.setEmail( userUpdateDto.getEmail() );
        }
        if ( userUpdateDto.getId() != null ) {
            user.setId( userUpdateDto.getId() );
        }
        if ( userUpdateDto.getProfileName() != null ) {
            user.setProfileName( userUpdateDto.getProfileName() );
        }
        if ( userUpdateDto.getUserSummary() != null ) {
            user.setUserSummary( userUpdateDto.getUserSummary() );
        }
        if ( userUpdateDto.getDob() != null ) {
            user.setDob( userUpdateDto.getDob() );
        }

        afterToUserFromUserUpdateDto( userUpdateDto, user );
    }

    protected Role roleDtoToRole(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        Role role = new Role();

        role.setAuthority( roleDto.getAuthority() );
        role.setId( roleDto.getId() );

        return role;
    }
}
