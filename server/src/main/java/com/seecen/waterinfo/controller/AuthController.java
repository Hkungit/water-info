package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.dto.auth.LoginRequest;
import com.seecen.waterinfo.dto.auth.LoginResponse;
import com.seecen.waterinfo.dto.user.UserSummary;
import com.seecen.waterinfo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success("logged out", null);
    }

    @GetMapping("/me")
    public ApiResponse<UserSummary> currentUser() {
        return ApiResponse.success(authService.currentUser());
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh() {
        LoginResponse response = LoginResponse.builder()
                .token("mock-token-" + UUID.randomUUID())
                .tokenType("Bearer")
                .expiresIn(86400)
                .build();
        return ApiResponse.success(response);
    }
}
