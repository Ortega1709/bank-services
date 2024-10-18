package com.ortega.user.role;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RoleRequest(
        UUID roleId,
        @NotNull(message = "Role name is required")
        String roleName
) {
}
