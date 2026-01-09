package com.seecen.waterinfo.service;

import com.seecen.waterinfo.common.PageResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seecen.waterinfo.domain.entity.User;
import com.seecen.waterinfo.domain.enums.UserRole;
import com.seecen.waterinfo.dto.user.CreateUserRequest;
import com.seecen.waterinfo.dto.user.UpdatePasswordRequest;
import com.seecen.waterinfo.dto.user.UpdateUserRequest;
import com.seecen.waterinfo.dto.user.UserSummary;
import com.seecen.waterinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResponse<UserSummary> list(int page, int size) {
        Page<User> pageRequest = new Page<>(Math.max(page, 1), size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("created_at");
        Page<User> users = userRepository.selectPage(pageRequest, wrapper);
        List<UserSummary> content = users.getRecords().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
        return PageResponse.of(content, users.getTotal(), (int) users.getPages(), (int) users.getCurrent(), (int) users.getSize());
    }

    public UserSummary detail(Long id) {
        User user = userRepository.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return toSummary(user);
    }

    @Transactional
    public UserSummary create(CreateUserRequest request) {
        User existingByName = userRepository.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        if (existingByName != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (request.getEmail() != null) {
            User existingByEmail = userRepository.selectOne(new QueryWrapper<User>().eq("email", request.getEmail()));
            if (existingByEmail != null) {
                throw new IllegalArgumentException("邮箱已存在");
            }
        }
        try {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .realName(request.getRealName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .role(Optional.ofNullable(request.getRole()).orElse(UserRole.VIEWER))
                    .status(1)
                    .build();
            userRepository.insert(user);
            return toSummary(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("用户信息已存在");
        }
    }

    @Transactional
    public void update(Long id, UpdateUserRequest request) {
        User user = userRepository.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            User existing = userRepository.selectOne(new QueryWrapper<User>().eq("email", request.getEmail()));
            if (existing != null) {
                throw new IllegalArgumentException("邮箱已存在");
            }
        }
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        userRepository.updateById(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updatePassword(Long id, UpdatePasswordRequest request) {
        User user = userRepository.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("原密码不正确");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.updateById(user);
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
