package com.simpl.user_role_client.dto;

public record RoleDto(
        String name,
        SCOPE scope) {

    public enum SCOPE {
        CLIENT, REALM
    }
}
