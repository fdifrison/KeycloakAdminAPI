package org.simpl.user_roles_resource.controller;

import org.simpl.user_roles_resource.dto.UserDto;
import org.simpl.user_roles_resource.service.IKeycloakRoleService;
import org.simpl.user_roles_resource.service.IKeycloakUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("kc/user")
@EnableMethodSecurity
public class KeycloakUserApiController {

    private final IKeycloakUserService userService;
    private final IKeycloakRoleService roleService;

    public KeycloakUserApiController(IKeycloakUserService userService, IKeycloakRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('user-roles.user')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    @PreAuthorize("hasRole('realm-roles.admin')")
    public ResponseEntity<UserDto> createRandomUser() {
        return userService.createRandomUser();
    }

    @PutMapping("realm/{username}/{roleName}")
    @PreAuthorize("hasRole('realm-roles.admin')")
    public ResponseEntity<Void> addRealmRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        return roleService.addRealmRoleToUser(username, roleName);
    }


}
