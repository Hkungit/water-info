package com.seecen.waterinfo.dto.auth;

import com.seecen.waterinfo.dto.user.UserSummary;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;
    private String tokenType;
    private long expiresIn;
    private UserSummary user;
}
