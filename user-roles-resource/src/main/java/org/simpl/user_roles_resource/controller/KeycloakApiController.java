package org.simpl.user_roles_resource.controller;

import org.simpl.user_roles_resource.dto.RoleDto;
import org.simpl.user_roles_resource.dto.UserDto;
import org.simpl.user_roles_resource.service.IKeycloakRoleService;
import org.simpl.user_roles_resource.service.IKeycloakUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("kc")
@EnableMethodSecurity
public class KeycloakApiController {

    private final IKeycloakUserService userService;
    private final IKeycloakRoleService roleService;

    public KeycloakApiController(IKeycloakUserService userService, IKeycloakRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('user-roles.user')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("user")
    @PreAuthorize("hasRole('realm-roles.admin')")
    public ResponseEntity<UserDto> createRandomUser() {
        return userService.createRandomUser();
    }

    @GetMapping("client/roles")
    @PreAuthorize("hasRole('user-roles.user')")
    public ResponseEntity<List<RoleDto>> getAllClientRoles() {
        return roleService.getClientRoles();
    }

    @PostMapping("client/roles")
    @PreAuthorize("hasRole('realm-roles.admin')")
    public ResponseEntity<RoleDto> createRandomClientRole() {
        return roleService.createRandomClientRole();
    }

    @GetMapping("realm/roles")
    @PreAuthorize("hasRole('user-roles.user')")
    public ResponseEntity<List<RoleDto>> getAllRealmRoles() {
        return roleService.getRealmRoles();
    }

    @PostMapping("realm/roles")
    @PreAuthorize("hasRole('realm-roles.admin')")
    public ResponseEntity<RoleDto> createRandomRealmRole() {
        return roleService.createRandomRealmRole();
    }

}
