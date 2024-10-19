package com.ortega.user.auth;

import com.ortega.user.exception.AuthException;
import com.ortega.user.user.User;
import com.ortega.user.user.UserDTO;
import com.ortega.user.user.UserMapper;
import com.ortega.user.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDTO login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new AuthException("Email or password incorrect")
        );

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException("Email or password incorrect");
        }

        return userMapper.toDTO(user);
    }

}
