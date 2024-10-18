package com.ortega.user.role;

import com.ortega.user.exception.RoleNotFoundException;
import com.ortega.user.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;


    public RoleDTO createRole(RoleRequest request) {
        Role role = roleRepository.save(roleMapper.toRole(request));
        return roleMapper.toDTO(role);

    }


    public RoleDTO updateRole(RoleRequest request) {
        roleRepository.findById(request.roleId()).orElseThrow(
                () -> new RoleNotFoundException(
                        String.format("Role with id '%s' not found", request.roleId())
                )
        );

        Role role = roleRepository.save(roleMapper.toRole(request));
        return roleMapper.toDTO(role);
    }


    public Page<RoleDTO> findAll(Pageable pageable) {
        return roleRepository
                .findAll(pageable)
                .map(roleMapper::toDTO);
    }


    public RoleDTO getRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
                .map(roleMapper::toDTO)
                .orElseThrow(
                        () -> new RoleNotFoundException(
                                String.format("Role with id '%s' not found", roleId)
                        )
                );
    }

    public void deleteRoleById(UUID roleId) {
        roleRepository.deleteById(roleId);
    }
}
