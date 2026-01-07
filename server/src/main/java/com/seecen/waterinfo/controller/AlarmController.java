package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.dto.alarm.AlarmResponse;
import com.seecen.waterinfo.service.AlarmService;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(alarmService.list(page, size));
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
    public ApiResponse<AlarmResponse> resolve(@PathVariable UUID id) {
        return ApiResponse.success("报警已解决", alarmService.resolve(id));
    }

    @GetMapping("/statistics")
    public ApiResponse<Object> statistics() {
        return ApiResponse.success(alarmService.statistics());
    }
}
