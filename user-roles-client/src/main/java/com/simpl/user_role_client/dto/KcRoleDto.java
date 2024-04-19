package com.simpl.user_role_client.dto;

import jakarta.validation.constraints.NotNull;

public record KcRoleDto(
        @NotNull String id,
        @NotNull String name,
        @NotNull SCOPE scope) {

    public enum SCOPE {
        CLIENT, REALM
    }
}
