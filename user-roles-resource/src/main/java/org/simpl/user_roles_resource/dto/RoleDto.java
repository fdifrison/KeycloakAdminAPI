package org.simpl.user_roles_resource.dto;

public record RoleDto(
        String name,
        SCOPE scope) {

    public enum SCOPE {
        CLIENT, REALM
    }
}
