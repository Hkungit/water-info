package com.seecen.waterinfo.dto.user;

import com.seecen.waterinfo.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String realName;

    @Email
    private String email;

    private String phone;

    private UserRole role;

    private Integer status;
}
