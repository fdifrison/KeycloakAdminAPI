package org.simpl.user_roles_resource.config.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    private final KeycloakProperties properties;

    public KeycloakClientConfig(KeycloakProperties properties) {
        this.properties = properties;
    }

    @Bean
    protected Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(properties.getApplicationRealm())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .serverUrl(properties.getUrl())
                .build();
    }

}
