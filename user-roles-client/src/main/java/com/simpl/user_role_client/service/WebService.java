package com.simpl.user_role_client.service;

import com.simpl.user_role_client.dto.RoleDto;
import com.simpl.user_role_client.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class WebService implements IWebService {

    @Value("${user-roles.resource.url}")
    private String API_URL;

    private final OAuth2AuthorizedClientService azdCliService;

    private final RestClient apiClient = RestClient.create();

    public WebService(OAuth2AuthorizedClientService azdCliService) {
        this.azdCliService = azdCliService;
    }

    @Override
    public List<UserDto> getAllUsers() {
        String token = getAccessToken();
        //TODO handle potential 403 from resource server
        return apiClient
                .get()
                .uri(API_URL + "/users")
                .header("Authorization", "bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public List<RoleDto> getRoles() {
        String token = getAccessToken();
        return apiClient
                .get()
                .uri(API_URL)
                .header("Authorization", "bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void addUser(UserDto user) {
        String token = getAccessToken();
        apiClient
                .post()
                .uri(API_URL)
                .header("Authorization", "bearer " + token)
                .body(user);

    }

    @Override
    public void addRole(RoleDto role) {
        String token = getAccessToken();
        apiClient
                .post()
                .uri(API_URL)
                .header("Authorization", "bearer " + token)
                .body(role);
    }

    private String getAccessToken() {
        var auth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String authId = auth.getAuthorizedClientRegistrationId();
        String name = auth.getName();

        log.info("Requesting access token for user [{}] with id [{}] ", name, authId);

        OAuth2AuthorizedClient authCli = azdCliService.loadAuthorizedClient(authId, name);
        OAuth2AccessToken token = authCli.getAccessToken();

        String tokenValue = token.getTokenValue();

        log.info("Access token: {}", tokenValue);

        return tokenValue;
    }
}
