package com.ortega.user.role;

import com.ortega.user.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createRole(@RequestBody @Valid RoleRequest request) {
        RoleDTO roleDTO = roleService.createRole(request);

        return SuccessResponse.builder()
                .status("success")
                .message("Role created successfully")
                .code(HttpStatus.CREATED.value())
                .data(roleDTO)
                .build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SuccessResponse updateRole(@RequestBody @Valid RoleRequest request) {
        RoleDTO roleDTO = roleService.updateRole(request);

        return SuccessResponse.builder()
                .status("success")
                .message("Role updated successfully")
                .code(HttpStatus.ACCEPTED.value())
                .data(roleDTO)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RoleDTO> roles = roleService.findAll(pageable);

        return SuccessResponse.builder()
                .status("success")
                .message("Roles fetched successfully")
                .code(HttpStatus.OK.value())
                .data(roles)
                .build();
    }

    @GetMapping("/{role-id}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findById(@PathVariable("role-id") UUID roleId) {
        RoleDTO roleDTO = roleService.getRoleById(roleId);

        return SuccessResponse.builder()
                .status("success")
                .message("Role found successfully")
                .code(HttpStatus.OK.value())
                .data(roleDTO)
                .build();
    }

    @DeleteMapping("/{role-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SuccessResponse delete(@PathVariable("role-id") UUID roleId) {
        roleService.deleteRoleById(roleId);

        return SuccessResponse.builder()
                .status("success")
                .message("Role deleted successfully")
                .code(HttpStatus.ACCEPTED.value())
                .data(null)
                .build();
    }

}
