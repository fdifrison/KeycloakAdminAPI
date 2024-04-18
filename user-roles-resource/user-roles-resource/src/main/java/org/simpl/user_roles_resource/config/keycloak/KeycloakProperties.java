package org.simpl.user_roles_resource.config.keycloak;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak-initializer")
public class KeycloakProperties {

    private String masterRealm;

    private String applicationRealm;

    private String clientId;

    private String clientSecret;

    private String username;

    private String password;

    private String url;
}
