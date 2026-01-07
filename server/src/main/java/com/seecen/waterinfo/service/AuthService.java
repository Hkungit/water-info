package com.seecen.waterinfo.service;

import com.seecen.waterinfo.domain.enums.UserRole;
import com.seecen.waterinfo.dto.auth.LoginRequest;
import com.seecen.waterinfo.dto.auth.LoginResponse;
import com.seecen.waterinfo.dto.user.UserSummary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    public LoginResponse login(LoginRequest request) {
        // TODO: replace with real authentication + JWT.
        UserSummary user = UserSummary.builder()
                .id(1L)
                .username(request.getUsername())
                .realName("系统管理员")
                .email("admin@waterinfo.com")
                .phone("13800138000")
                .role(UserRole.ADMIN)
                .status(1)
                .lastLoginAt(LocalDateTime.now())
                .build();

        return LoginResponse.builder()
                .token("mock-token-" + UUID.randomUUID())
                .tokenType("Bearer")
                .expiresIn(86400)
                .user(user)
                .build();
    }

    public UserSummary currentUser() {
        return UserSummary.builder()
                .id(1L)
                .username("admin")
                .realName("系统管理员")
                .email("admin@waterinfo.com")
                .phone("13800138000")
                .role(UserRole.ADMIN)
                .status(1)
                .lastLoginAt(LocalDateTime.now())
                .build();
    }
}
