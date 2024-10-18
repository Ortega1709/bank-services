package com.ortega.user.role;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toRole(RoleRequest roleRequest) {
        if (roleRequest == null) {
            return null;
        }

        return new Role(
                roleRequest.roleId(),
                roleRequest.roleName()
        );
    }

    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }

        return new RoleDTO(
                role.getRoleId(),
                role.getRoleName()
        );
    }

}
