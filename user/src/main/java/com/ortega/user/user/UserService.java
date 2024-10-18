package com.ortega.user.user;

import com.ortega.user.exception.RoleNotFoundException;
import com.ortega.user.exception.UserNotFoundException;
import com.ortega.user.role.Role;
import com.ortega.user.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserDTO createUser(UserRequest request) {
        Set<Role> roles = getRolesByRoleIds(request.roleIds());

        User user = User.builder()
                .userId(request.userId())
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .roles(roles)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO updateUser(UserRequest request) {
        User userSaved = userRepository.findById(request.userId()).orElseThrow(
                () -> new UserNotFoundException(
                        String.format("User with id %s not found", request.userId())
                )
        );

        Set<Role> roles = getRolesByRoleIds(request.roleIds());
        User user = User.builder()
                .userId(request.userId())
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .roles(roles)
                .createdAt(userSaved.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return userMapper.toDTO(userRepository.save(user));
    }

    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(userMapper::toDTO);
    }

    public UserDTO getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDTO)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("User with id %s not found", userId)
                        )
                );
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }

    private Set<Role> getRolesByRoleIds(Set<UUID> roleIds) {
        return roleIds
                .stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(
                                () -> new RoleNotFoundException(
                                        String.format("Role with id %s not found", roleId)
                                )
                        )
                ).collect(Collectors.toSet());
    }
}
