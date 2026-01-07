package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.dto.monitoring.FlowRecordRequest;
import com.seecen.waterinfo.dto.monitoring.FlowRecordResponse;
import com.seecen.waterinfo.service.MonitoringService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flow-monitoring")
public class FlowMonitoringController {

    private final MonitoringService monitoringService;

    public FlowMonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping("/latest")
    public ApiResponse<List<FlowRecordResponse>> latest(@RequestParam(required = false) String stationId) {
        return ApiResponse.success(monitoringService.latestFlowRecords(stationId));
    }

    @GetMapping("/station/{stationId}")
    public ApiResponse<PageResponse<FlowRecordResponse>> history(
            @PathVariable String stationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(monitoringService.flowHistory(stationId, page, size));
    }

    @GetMapping("/latest/one")
    public ApiResponse<FlowRecordResponse> latestOne() {
        return ApiResponse.success(monitoringService.latestFlowRecordOne());
    }

    @PostMapping
    public ApiResponse<FlowRecordResponse> create(@Valid @RequestBody FlowRecordRequest request) {
        return ApiResponse.success("流量记录添加成功", monitoringService.addFlowRecord(request));
    }

    @GetMapping("/statistics")
    public ApiResponse<Object> statistics() {
        return ApiResponse.success(monitoringService.flowStats());
    }
}
