package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.dto.export.ExportRequest;
import com.seecen.waterinfo.service.ExportService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping("/water-levels")
    public ApiResponse<String> exportWaterLevels(@Valid @RequestBody ExportRequest request) {
        return ApiResponse.success(exportService.exportWaterLevels(request));
    }

    @PostMapping("/flow-monitoring")
    public ApiResponse<String> exportFlowMonitoring(@Valid @RequestBody ExportRequest request) {
        return ApiResponse.success(exportService.exportFlowMonitoring(request));
    }

    @PostMapping("/water-quality")
    public ApiResponse<String> exportWaterQuality(@Valid @RequestBody ExportRequest request) {
        return ApiResponse.success(exportService.exportWaterQuality(request));
    }

    @PostMapping("/alarms")
    public ApiResponse<String> exportAlarms(@Valid @RequestBody ExportRequest request) {
        return ApiResponse.success(exportService.exportAlarms(request));
    }

    @PostMapping("/report")
    public ApiResponse<String> exportReport(@Valid @RequestBody ExportRequest request) {
        return ApiResponse.success(exportService.exportReport(request));
    }
}
