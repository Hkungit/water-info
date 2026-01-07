package com.seecen.waterinfo.service;

import com.seecen.waterinfo.domain.enums.UserRole;
import com.seecen.waterinfo.dto.user.CreateUserRequest;
import com.seecen.waterinfo.dto.user.UpdatePasswordRequest;
import com.seecen.waterinfo.dto.user.UpdateUserRequest;
import com.seecen.waterinfo.dto.user.UserSummary;
import com.seecen.waterinfo.common.PageResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public PageResponse<UserSummary> list(int page, int size) {
        List<UserSummary> users = new ArrayList<>();
        users.add(sampleAdmin());
        users.add(UserSummary.builder()
                .id(2L)
                .username("operator1")
                .realName("操作员")
                .email("operator1@waterinfo.com")
                .phone("13800138001")
                .role(UserRole.OPERATOR)
                .status(1)
                .createdAt(LocalDateTime.now().minusDays(1))
                .lastLoginAt(LocalDateTime.now().minusHours(1))
                .build());
        return PageResponse.of(users, users.size(), 1, page, size);
    }

    public UserSummary detail(Long id) {
        return sampleAdmin().toBuilder().id(id).build();
    }

    public UserSummary create(CreateUserRequest request) {
        return UserSummary.builder()
                .id(3L)
                .username(request.getUsername())
                .realName(request.getRealName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(request.getRole())
                .status(1)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void update(Long id, UpdateUserRequest request) {
        // TODO: implement persistence update
    }

    public void delete(Long id) {
        // TODO: implement delete
    }

    public void updatePassword(Long id, UpdatePasswordRequest request) {
        // TODO: implement password change
    }

    private UserSummary sampleAdmin() {
        return UserSummary.builder()
                .id(1L)
                .username("admin")
                .realName("系统管理员")
                .email("admin@waterinfo.com")
                .phone("13800138000")
                .role(UserRole.ADMIN)
                .status(1)
                .createdAt(LocalDateTime.now().minusDays(3))
                .lastLoginAt(LocalDateTime.now())
                .build();
    }
}
