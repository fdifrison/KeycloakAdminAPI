package com.simpl.user_role_client.dto;

import jakarta.validation.constraints.NotNull;

public record RoleDto(
        @NotNull String name,
        @NotNull SCOPE scope) {

    public enum SCOPE {
        CLIENT, REALM
    }
}
