package com.simpl.user_role_client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig {

    // Repository of all the clients defined in application.yml
    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2LoginSecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/user-and-roles/home").hasRole("user-roles.user")
                                .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(
                        oauth2 ->
                                oauth2.userInfoEndpoint(epConfig -> epConfig.oidcUserService(this.userService())))
                .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()));

        return http.build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> userService() {

        final OidcUserService delegate = new OidcUserService();
        return (userRequest) -> {

            // Delegate to the default implementation for loading a user
            OidcUser oidcUser = delegate.loadUser(userRequest);

            List<String> roles = oidcUser.getIdToken().getClaim("roles");
            List<String> realm_roles = oidcUser.getIdToken().getClaim("realm_roles");

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            roles.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);
            realm_roles.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);

            Set<GrantedAuthority> mappedAuthorities = new HashSet<>(authorities);

            // Create new DefaultOidcUser with authorities
            return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };

    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        var logoutHandler = new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
        // redirect after logout; has to match what has been set in kc client configuration
        logoutHandler.setPostLogoutRedirectUri("{baseUrl}/user-and-roles/home");
        return logoutHandler;
    }

}
