package com.ortega.user.auth;

import com.ortega.user.response.AuthResponse;
import com.ortega.user.user.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody @Valid AuthRequest authRequest) {
        UserDTO userDTO = authService.login(authRequest);

        return AuthResponse.builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Authentication success")
                .data(userDTO)
                .token("eyJhbGciOiJIUzI1NiJ9bIn5nCekdedg9.IhnUIinOpGn4IR15NBEnVV5oC")
                .build();
    }

}
