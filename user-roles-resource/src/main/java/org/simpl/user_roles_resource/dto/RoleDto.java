package org.simpl.user_roles_resource.dto;

import jakarta.validation.constraints.NotNull;

public record RoleDto(
        @NotNull String id,
        @NotNull String name,
        @NotNull SCOPE scope) {

    public enum SCOPE {
        CLIENT, REALM
    }
}
