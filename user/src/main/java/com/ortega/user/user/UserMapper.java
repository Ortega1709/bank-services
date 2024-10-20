package com.ortega.user.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User toUser(UserRequest request) {
        if (request == null) return null;

        return User.builder()
                .userId(request.userId())
                .email(request.email())
                .username(request.username())
                .build();
    }

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
