package org.simpl.user_roles_resource.service;

import jakarta.ws.rs.ClientErrorException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.simpl.user_roles_resource.dto.RoleDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


// TODO refactor kc methods, most are repeated

@Slf4j
@Service
public class KeycloakRoleService implements IKeycloakRoleService {

    @Value("${keycloak-initializer.applicationRealm}")
    private String realm;
    private final String clientId = "user-roles";
    private final Keycloak keycloak;

    public KeycloakRoleService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }


    @Override
    public ResponseEntity<RoleDto> createRandomRealmRole() {
        int rnd = (int) ((1000000) * Math.random());
        var roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName("Random Realm Role " + rnd);
        try {
            keycloak.realm(this.realm).roles().create(roleRepresentation);
        } catch (ClientErrorException e) {
            log.error("Failed to create random realm role", e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                new RoleDto(roleRepresentation.getId(), roleRepresentation.getName(), RoleDto.SCOPE.REALM));
    }

    @Override
    public ResponseEntity<RoleDto> createRandomClientRole() {
        int rnd = (int) ((1000000) * Math.random());
        var roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName("Random Client Role " + rnd);
        try {
            var client = keycloak
                    .realm(this.realm)
                    .clients()
                    .findByClientId(clientId);
            if (client != null && !client.isEmpty()) {
                keycloak
                        .realm(this.realm)
                        .clients().
                        get(client.get(0).getId())
                        .roles()
                        .create(roleRepresentation);
            }
        } catch (ClientErrorException e) {
            log.error("Failed to create random realm role", e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                new RoleDto(roleRepresentation.getId(), roleRepresentation.getName(), RoleDto.SCOPE.REALM));
    }

    @Override
    public ResponseEntity<List<RoleDto>> getRealmRoles() {
        List<RoleRepresentation> realmRoles;
        try {
            realmRoles = new ArrayList<>(keycloak.realm(this.realm).roles().list());
        } catch (ClientErrorException e) {
            log.error("Failed to get realm roles", e);
            return ResponseEntity.badRequest().build();
        }
        if (realmRoles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var roles = realmRoles.stream()
                .map(r -> new RoleDto(r.getId(), r.getName(), RoleDto.SCOPE.REALM))
                .collect(Collectors.toList());

        return ResponseEntity.ok(roles);
    }

    @Override
    public ResponseEntity<List<RoleDto>> getClientRoles() {
        List<RoleRepresentation> clientRoles = new ArrayList<>();
        try {
            var client = keycloak
                    .realm(this.realm)
                    .clients()
                    .findByClientId(clientId);
            if (client != null && !client.isEmpty()) {
                clientRoles.addAll(keycloak
                        .realm(this.realm)
                        .clients()
                        .get(client.get(0).getId())
                        .roles().list());
            }
        } catch (ClientErrorException e) {
            log.error("Failed to get client roles", e);
            ResponseEntity.badRequest().build();
        }
        if (clientRoles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var roles = clientRoles.stream()
                .map(r -> new RoleDto(r.getId(), r.getName(), RoleDto.SCOPE.CLIENT))
                .collect(Collectors.toList());

        return ResponseEntity.ok(roles);
    }


    @Override
    public ResponseEntity<Void> addRealmRoleToUser(String userName, String roleName) {

        try {
            UserResource user = getUserResourceByUsername(userName);

            List<RoleRepresentation> roleToAdd = new LinkedList<>();

            roleToAdd.add(keycloak
                    .realm(realm)
                    .clients()
                    .get(clientId)
                    .roles()
                    .get(roleName)
                    .toRepresentation());

            user.roles().clientLevel(clientId).add(roleToAdd);

        } catch (ClientErrorException e) {
            log.error("Failed to add realm role", e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    private String getUserIdFromUserName(String userName) {
        return keycloak
                .realm(realm)
                .users()
                .search(userName)
                .get(0)
                .getId();
    }

    private UserResource getUserResourceByUsername(String username) {
        return keycloak
                .realm(realm)
                .users()
                .get(getUserIdFromUserName(username));
    }
}
