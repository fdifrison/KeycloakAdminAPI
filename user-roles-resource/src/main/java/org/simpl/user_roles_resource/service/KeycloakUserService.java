package org.simpl.user_roles_resource.service;

import jakarta.ws.rs.ClientErrorException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.simpl.user_roles_resource.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class KeycloakUserService implements IKeycloakUserService {

    @Value("${keycloak-initializer.applicationRealm}")
    private String realm;
    private final Keycloak keycloak;

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userList = new ArrayList<>();
        var users = keycloak.realm(realm).users().list();
        try {
            userList.addAll(users.stream()
                    .map(u ->
                            new UserDto(u.getId(), u.getFirstName(), u.getLastName(), u.getUsername(), u.getEmail()))
                    .toList());

        } catch (ClientErrorException e) {
            log.error("Get all users failed", e);
        }
        if (userList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userList);
    }

    @Override
    public ResponseEntity<UserDto> createRandomUser() {
        var user = createRandomUserRepresentation();
        var response = keycloak.realm(this.realm).users().create(user);
        if (Objects.equals(201, response.getStatusInfo().getStatusCode())) {
            var newUser = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(),
                    user.getEmail());
            response.close();
            return ResponseEntity.ok(newUser);
        }
        log.error("Failed to create random user");
        return ResponseEntity.badRequest().build();
    }


    private static UserRepresentation createRandomUserRepresentation() {
        int rnd = (int) ((1000000) * Math.random());
        String base = "RandomUser";
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(base + rnd + "@gmail.com");
        userRepresentation.setUsername(base + rnd);
        userRepresentation.setFirstName("Random");
        userRepresentation.setLastName("User" + rnd);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        CredentialRepresentation userCredentialRepresentation = new CredentialRepresentation();
        userCredentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        userCredentialRepresentation.setTemporary(false);
        userCredentialRepresentation.setValue(Integer.toString(rnd));
        userRepresentation.setCredentials(List.of(userCredentialRepresentation));
        return userRepresentation;
    }
}
