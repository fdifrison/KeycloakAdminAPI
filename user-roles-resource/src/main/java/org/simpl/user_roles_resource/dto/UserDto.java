package org.simpl.user_roles_resource.dto;

import jakarta.validation.constraints.NotNull;

public record UserDto(
        @NotNull String id,
        @NotNull String name,
        @NotNull String surname,
        @NotNull String username,
        @NotNull String email) {
}
