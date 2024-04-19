package org.simpl.user_roles_resource.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Collection<String> roles = jwt.getClaim("roles");
        Collection<String> realm_roles = jwt.getClaim("realm_roles");

        roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role)));
        realm_roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role)));

        log.info("Authorities : {}", grantedAuthorities);

        return grantedAuthorities;

    }
}
