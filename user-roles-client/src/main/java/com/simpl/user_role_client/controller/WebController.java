package com.simpl.user_role_client.controller;

import com.simpl.user_role_client.dto.RoleDto;
import com.simpl.user_role_client.service.IWebService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    private final IWebService webService;

    public WebController(IWebService webService) {
        this.webService = webService;
    }

    @GetMapping("/")
    public String slash() {
        return "redirect:/user-and-roles/home";
    }

    @GetMapping("/user-and-roles/home")
    public ModelAndView home(OAuth2AuthenticationToken token) {

        ModelAndView model = generateDefaultModel(token);

        model.setViewName("user-home");

        return model;
    }

    @PostMapping("/user-and-roles/home/createRandomUser")
    public ModelAndView addRandomUser(OAuth2AuthenticationToken token) {
        ModelAndView model = generateDefaultModel(token);
        webService.addRandomUser();
        model.addObject("users", webService.getAllUsers());
        model.setViewName("user-home");
        return model;
    }

    @PostMapping("/user-and-roles/home/createRandomClientRole")
    public ModelAndView addRandomClientRole(OAuth2AuthenticationToken token) {
        ModelAndView model = generateDefaultModel(token);
        webService.addRandomClientRole();
        model.addObject("clientRoles", webService.getClientRoles());
        model.setViewName("user-home");
        return model;
    }

    @PostMapping("/user-and-roles/home/createRandomRealmRole")
    public ModelAndView addRandomRealmRole(OAuth2AuthenticationToken token) {
        ModelAndView model = generateDefaultModel(token);
        webService.addRandomRealmRole();
        model.addObject("clientRoles", webService.getRealmRoles());
        model.setViewName("user-home");
        return model;
    }


    /*
     * Sets some basic user information. The call can add more properties
     * to it before passing to the view file.
     */
    private ModelAndView generateDefaultModel(OAuth2AuthenticationToken token) {

        OidcUser principal = (OidcUser) token.getPrincipal();
        ModelAndView model = new ModelAndView();

        var name = principal.getUserInfo().getClaim("preferred_username");
        List<String> roles = principal.getUserInfo().getClaim("roles");
        List<String> realm_roles = principal.getUserInfo().getClaim("realm_roles");

        List<RoleDto> all_roles = new ArrayList<>();

        roles.stream().map(r -> new RoleDto(r, RoleDto.SCOPE.CLIENT)).forEach(all_roles::add);
        realm_roles.stream().map(r -> new RoleDto(r, RoleDto.SCOPE.REALM)).forEach(all_roles::add);

        model.addObject("name", name);
        model.addObject("roles", all_roles);

        model.addObject("users", webService.getAllUsers());
        model.addObject("clientRoles", webService.getClientRoles());
        model.addObject("realmRoles", webService.getRealmRoles());

        return model;
    }
}
