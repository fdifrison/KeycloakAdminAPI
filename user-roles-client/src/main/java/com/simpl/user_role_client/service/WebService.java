package com.simpl.user_role_client.service;

import com.simpl.user_role_client.dto.KcRoleDto;
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
import java.util.Objects;

@Slf4j
@Service
public class WebService implements IWebService {

    @Value("${user-roles.resource.url}")
    private String API_URL;

    private final OAuth2AuthorizedClientService authCliService;

    private final RestClient apiClient = RestClient.create();

    public WebService(OAuth2AuthorizedClientService authCliService) {
        this.authCliService = authCliService;
    }

    @Override
    public List<UserDto> getAllUsers() {
        String token = getAccessToken();
        return apiClient
                .get()
                .uri(API_URL + "/user")
                .header("Authorization", "bearer " + token)
                .exchange((req, res) -> {
                    if (res.getStatusCode().is2xxSuccessful()) {
                        return Objects.requireNonNull(res.bodyTo(new ParameterizedTypeReference<>() {
                        }));
                    }
                    return List.of();
                });
    }

    @Override
    public void addRandomUser() {
        String token = getAccessToken();
        apiClient
                .post()
                .uri(API_URL + "/user")
                .header("Authorization", "bearer " + token)
                .retrieve();

    }

    @Override
    public List<KcRoleDto> getClientRoles() {
        String token = getAccessToken();
        return apiClient
                .get()
                .uri(API_URL + "/roles/client")
                .header("Authorization", "bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void addRandomClientRole() {
        String token = getAccessToken();
        apiClient
                .post()
                .uri(API_URL + "/roles/client")
                .header("Authorization", "bearer " + token)
                .retrieve();
    }

    @Override
    public List<KcRoleDto> getRealmRoles() {
        String token = getAccessToken();
        return apiClient
                .get()
                .uri(API_URL + "/roles/realm")
                .header("Authorization", "bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void addRandomRealmRole() {
        String token = getAccessToken();
        apiClient
                .post()
                .uri(API_URL + "/roles/realm")
                .header("Authorization", "bearer " + token)
                .retrieve();
    }


    private String getAccessToken() {
        var auth = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String authId = auth.getAuthorizedClientRegistrationId();
        String name = auth.getName();

        log.info("Requesting access token for user [{}] with id [{}] ", name, authId);

        OAuth2AuthorizedClient authCli = authCliService.loadAuthorizedClient(authId, name);
        OAuth2AccessToken token = authCli.getAccessToken();

        String tokenValue = token.getTokenValue();

        log.info("Access token: {}", tokenValue);

        return tokenValue;
    }
}
