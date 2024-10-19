package com.ortega.user.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
        @NotNull(message = "User email is required")
        @Email(message = "User email is not valid email address")
        String email,
        @NotNull(message = "User password is required")
        String password
) {
}
