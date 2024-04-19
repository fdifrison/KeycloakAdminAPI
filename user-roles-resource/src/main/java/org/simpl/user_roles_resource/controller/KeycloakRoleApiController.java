package org.simpl.user_roles_resource.controller;

import org.simpl.user_roles_resource.dto.RoleDto;
import org.simpl.user_roles_resource.service.IKeycloakRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("kc/roles")
@EnableMethodSecurity
public class KeycloakRoleApiController {


    private final IKeycloakRoleService roleService;

    public KeycloakRoleApiController(IKeycloakRoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("client")
    @PreAuthorize("hasRole('user-roles.user')")
    public ResponseEntity<List<RoleDto>> getAllClientRoles() {
        return roleService.getClientRoles();
    }

    @PostMapping("client")
    @PreAuthorize("hasRole('realm-roles.admin')")
    public ResponseEntity<RoleDto> createRandomClientRole() {
        return roleService.createRandomClientRole();
    }

    @GetMapping("realm")
    @PreAuthorize("hasRole('user-roles.user')")
    public ResponseEntity<List<RoleDto>> getAllRealmRoles() {
        return roleService.getRealmRoles();
    }

    @PostMapping("realm")
    @PreAuthorize("hasRole('realm-roles.admin')")
    public ResponseEntity<RoleDto> createRandomRealmRole() {
        return roleService.createRandomRealmRole();
    }

}
