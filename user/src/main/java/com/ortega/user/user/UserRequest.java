package com.ortega.user.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record UserRequest(
        UUID userId,
        @NotNull(message = "Username is required")
        String username,
        @NotNull(message = "User email is required")
        @Email(message = "User email is not valid email address")
        String email,
        @NotNull(message = "User password is required")
        String password,
        @NotNull(message = "User roles is required")
        Set<UUID> roleIds
) {

}
