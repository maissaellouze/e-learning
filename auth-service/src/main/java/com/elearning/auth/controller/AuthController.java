package com.elearning.auth.controller;

import com.elearning.auth.dto.LoginRequest;
import com.elearning.auth.dto.LoginResponse;
import com.elearning.auth.dto.RegisterRequest;
import com.elearning.auth.service.AuthService;
import com.elearning.common.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.getUserById(userId));
    }
}
