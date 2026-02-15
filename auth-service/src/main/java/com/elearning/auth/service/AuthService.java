package com.elearning.auth.service;

import com.elearning.auth.dto.LoginRequest;
import com.elearning.auth.dto.LoginResponse;
import com.elearning.auth.dto.RegisterRequest;
import com.elearning.auth.entity.User;
import com.elearning.auth.entity.UserRole;
import com.elearning.auth.repository.UserRepository;
import com.elearning.common.dto.UserDTO;
import com.elearning.common.security.JwtTokenProvider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getEmail(), user.getRole().toString());

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }

    public UserDTO register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Default role to STUDENT if not provided
        String roleStr = request.getRole() != null ? request.getRole() : "STUDENT";

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(UserRole.valueOf(roleStr.toUpperCase()))
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return UserDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .role(savedUser.getRole().toString())
                .build();
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .build();
    }
}
