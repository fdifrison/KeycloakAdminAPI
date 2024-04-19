package org.simpl.user_roles_resource.service;

import org.simpl.user_roles_resource.dto.UserDto;

import java.util.List;

public interface IKeycloakUserService {

    List<UserDto> getAllUsers();

}
