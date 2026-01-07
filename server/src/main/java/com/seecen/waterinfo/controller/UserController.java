package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.dto.user.CreateUserRequest;
import com.seecen.waterinfo.dto.user.UpdatePasswordRequest;
import com.seecen.waterinfo.dto.user.UpdateUserRequest;
import com.seecen.waterinfo.dto.user.UserSummary;
import com.seecen.waterinfo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<PageResponse<UserSummary>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(userService.list(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserSummary> detail(@PathVariable Long id) {
        return ApiResponse.success(userService.detail(id));
    }

    @PostMapping
    public ApiResponse<UserSummary> create(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.success("用户创建成功", userService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        userService.update(id, request);
        return ApiResponse.success("用户更新成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success("用户删除成功", null);
    }

    @PutMapping("/{id}/password")
    public ApiResponse<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(id, request);
        return ApiResponse.success("密码修改成功", null);
    }
}
