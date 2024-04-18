package org.simpl.user_roles_resource.service;

import org.keycloak.admin.client.Keycloak;
import org.simpl.user_roles_resource.dto.RoleDto;
import org.simpl.user_roles_resource.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakUserService implements IKeycloakUserService {

    @Value("${keycloak-initializer.applicationRealm}")
    private String realm;
    private final Keycloak keycloak;

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public List<UserDto> getAllUsers() {
        var users = keycloak.realm(realm).users().list();
        return users.stream().map(u -> {
//            var roles = u.getRealmRoles()
//                    .stream()
//                    .map( r-> new RoleDto(r, RoleDto.SCOPE.REALM))
//                    .collect(Collectors.toList());
            return new UserDto(u.getFirstName(), u.getLastName(), u.getUsername(), List.of(new RoleDto("fake", RoleDto.SCOPE.CLIENT)));
        }).collect(Collectors.toList());
    }
}
