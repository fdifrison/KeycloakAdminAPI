package org.simpl.user_roles_resource.controller;

import org.simpl.user_roles_resource.dto.UserDto;
import org.simpl.user_roles_resource.service.IKeycloakUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kc/users")
@EnableMethodSecurity
public class KeycloakApiController {

    private final IKeycloakUserService userService;

    public KeycloakApiController(IKeycloakUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('realm-roles.admin')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

}
