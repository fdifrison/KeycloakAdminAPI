package org.simpl.user_roles_resource.service;

import org.simpl.user_roles_resource.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IKeycloakUserService {

    ResponseEntity<List<UserDto>> getAllUsers();
    ResponseEntity<UserDto> createRandomUser();


}
