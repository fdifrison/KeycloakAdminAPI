package com.simpl.user_role_client.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDto(
        @NotNull String name,
        @NotNull String surname,
        @NotNull String username,
        List<RoleDto> roles) {
}
