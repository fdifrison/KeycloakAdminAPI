package com.simpl.user_role_client.controller;

import com.simpl.user_role_client.service.IWebService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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

        OidcUser principal = (OidcUser) token.getPrincipal();

        ModelAndView model = generateDefaultModel(token);

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
        model.addObject("user", principal);
        return model;
    }
}
