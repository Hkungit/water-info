package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.dto.monitoring.WaterQualityRequest;
import com.seecen.waterinfo.dto.monitoring.WaterQualityResponse;
import com.seecen.waterinfo.service.MonitoringService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/water-quality")
public class WaterQualityController {

    private final MonitoringService monitoringService;

    public WaterQualityController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping("/latest")
    public ApiResponse<List<WaterQualityResponse>> latest(@RequestParam(required = false) String stationId) {
        return ApiResponse.success(monitoringService.latestWaterQuality(stationId));
    }

    @GetMapping("/station/{stationId}")
    public ApiResponse<PageResponse<WaterQualityResponse>> history(
            @PathVariable String stationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(monitoringService.waterQualityHistory(stationId, page, size));
    }

    @PostMapping
    public ApiResponse<WaterQualityResponse> create(@Valid @RequestBody WaterQualityRequest request) {
        return ApiResponse.success("水质记录添加成功", monitoringService.addWaterQuality(request));
    }
}
