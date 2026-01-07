package com.seecen.waterinfo.dto.user;

import com.seecen.waterinfo.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank
    @Size(min = 3, max = 64)
    private String username;

    @NotBlank
    @Size(min = 6, max = 64)
    private String password;

    @NotBlank
    private String realName;

    @Email
    private String email;

    private String phone;

    private UserRole role = UserRole.VIEWER;
}
