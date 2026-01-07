package com.seecen.waterinfo.dto.user;

import com.seecen.waterinfo.domain.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class UserSummary {
    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private UserRole role;
    private Integer status;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
