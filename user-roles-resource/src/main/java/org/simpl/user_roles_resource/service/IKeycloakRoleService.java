package org.simpl.user_roles_resource.service;

import org.simpl.user_roles_resource.dto.RoleDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IKeycloakRoleService {

    ResponseEntity<RoleDto> createRandomRealmRole();
    ResponseEntity<RoleDto> createRandomClientRole();
    ResponseEntity<List<RoleDto>> getRealmRoles();
    ResponseEntity<List<RoleDto>> getClientRoles();
    ResponseEntity<Void> addRealmRoleToUser(String userName, String roleName);

}
