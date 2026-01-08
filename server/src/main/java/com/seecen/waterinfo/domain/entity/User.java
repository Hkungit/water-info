package com.seecen.waterinfo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seecen.waterinfo.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User extends BaseTimeEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    @TableField("real_name")
    private String realName;

    private String email;

    private String phone;

    private UserRole role = UserRole.VIEWER;

    private Integer status = 1;

    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;
}
