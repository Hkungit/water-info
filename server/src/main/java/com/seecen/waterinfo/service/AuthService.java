package com.seecen.waterinfo.service;

import com.seecen.waterinfo.domain.entity.User;
import com.seecen.waterinfo.dto.auth.LoginRequest;
import com.seecen.waterinfo.dto.auth.LoginResponse;
import com.seecen.waterinfo.dto.user.UserSummary;
import com.seecen.waterinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        if (user == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.updateById(user);

        String token = UUID.randomUUID().toString();
        sessions.put(token, user.getId());

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400)
                .user(toSummary(user))
                .build();
    }

    public void logout(String authHeader) {
        String token = extractToken(authHeader);
        if (token != null) {
            sessions.remove(token);
        }
    }

    public LoginResponse refresh(String authHeader) {
        Long userId = requireUserId(authHeader);
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        String token = UUID.randomUUID().toString();
        sessions.put(token, userId);
        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400)
                .user(toSummary(user))
                .build();
    }

    public UserSummary currentUser(String authHeader) {
        Long userId = requireUserId(authHeader);
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return toSummary(user);
    }

    private Long requireUserId(String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) {
            throw new IllegalArgumentException("未登录或登录已失效");
        }
        Long userId = sessions.get(token);
        if (userId == null) {
            throw new IllegalArgumentException("登录已失效");
        }
        return userId;
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private UserSummary toSummary(User user) {
        return UserSummary.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
