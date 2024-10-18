package com.ortega.user.user;


import com.ortega.user.role.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Builder
public class UserDTO {
    private UUID userId;
    private String username;
    private String email;
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
