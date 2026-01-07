package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.dto.monitoring.WaterLevelRequest;
import com.seecen.waterinfo.dto.monitoring.WaterLevelResponse;
import com.seecen.waterinfo.service.MonitoringService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/water-levels")
public class WaterLevelController {

    private final MonitoringService monitoringService;

    public WaterLevelController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping("/latest")
    public ApiResponse<List<WaterLevelResponse>> latest(@RequestParam(required = false) String stationId) {
        return ApiResponse.success(monitoringService.latestWaterLevels(stationId));
    }

    @GetMapping("/station/{stationId}")
    public ApiResponse<PageResponse<WaterLevelResponse>> history(
            @PathVariable String stationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(monitoringService.waterLevelHistory(stationId, page, size));
    }

    @GetMapping("/latest/one")
    public ApiResponse<WaterLevelResponse> latestOne() {
        return ApiResponse.success(monitoringService.latestWaterLevelOne());
    }

    @PostMapping
    public ApiResponse<WaterLevelResponse> create(@Valid @RequestBody WaterLevelRequest request) {
        return ApiResponse.success("水位记录添加成功", monitoringService.addWaterLevel(request));
    }

    @GetMapping("/statistics")
    public ApiResponse<Object> statistics() {
        return ApiResponse.success(monitoringService.waterLevelStats());
    }
}
