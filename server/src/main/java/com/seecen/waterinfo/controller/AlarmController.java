package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.config.security.UserPrincipal;
import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.domain.enums.AlarmStatus;
import com.seecen.waterinfo.dto.alarm.AlarmResponse;
import com.seecen.waterinfo.service.AlarmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;

@RestController
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping
    public ApiResponse<PageResponse<AlarmResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        return ApiResponse.success(alarmService.list(page, size, status == null ? null : AlarmStatus.fromValue(status)));
    }

    @GetMapping("/active/count")
    public ApiResponse<Long> activeCount() {
        return ApiResponse.success(alarmService.activeCount());
    }

    @GetMapping("/{id}")
    public ApiResponse<AlarmResponse> detail(@PathVariable UUID id) {
        return ApiResponse.success(alarmService.detail(id));
    }

    @PutMapping("/{id}/resolve")
    public ApiResponse<AlarmResponse> resolve(@PathVariable UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        Long resolverId = principal == null ? null : principal.getId();
        return ApiResponse.success("报警处理成功", alarmService.resolve(id, resolverId));
    }

    @GetMapping("/statistics")
    public ApiResponse<Object> statistics() {
        return ApiResponse.success(alarmService.statistics());
    }
}
