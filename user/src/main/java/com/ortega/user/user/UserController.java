package com.ortega.user.user;

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
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createUser(@RequestBody @Valid UserRequest request) {
        UserDTO userDTO = userService.createUser(request);

        return SuccessResponse.builder()
                .status("success")
                .message("User created successfully")
                .code(HttpStatus.CREATED.value())
                .data(userDTO)
                .build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SuccessResponse updateUser(@RequestBody @Valid UserRequest request) {
        UserDTO userDTO = userService.updateUser(request);

        return SuccessResponse.builder()
                .status("success")
                .message("User updated successfully")
                .code(HttpStatus.ACCEPTED.value())
                .data(userDTO)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users = userService.findAll(pageable);

        return SuccessResponse.builder()
                .status("success")
                .message("Users fetched successfully")
                .code(HttpStatus.OK.value())
                .data(users)
                .build();
    }

    @GetMapping("/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse findById(@PathVariable("user-id") UUID userId) {
        UserDTO userDTO = userService.getUserById(userId);

        return SuccessResponse.builder()
                .status("success")
                .message("User fetched successfully")
                .code(HttpStatus.OK.value())
                .data(userDTO)
                .build();
    }

    @DeleteMapping("/{user-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SuccessResponse delete(@PathVariable("user-id") UUID userId) {
        userService.deleteUserById(userId);

        return SuccessResponse.builder()
                .status("success")
                .message("User delete successfully")
                .code(HttpStatus.ACCEPTED.value())
                .data(null)
                .build();
    }
}
