package com.seecen.waterinfo.controller;

import com.seecen.waterinfo.common.ApiResponse;
import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.dto.station.StationRequest;
import com.seecen.waterinfo.dto.station.StationResponse;
import com.seecen.waterinfo.service.StationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ApiResponse<PageResponse<StationResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(stationService.list(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<StationResponse> detail(@PathVariable UUID id) {
        return ApiResponse.success(stationService.detail(id));
    }

    @PostMapping
    public ApiResponse<StationResponse> create(@Valid @RequestBody StationRequest request) {
        return ApiResponse.success("站点创建成功", stationService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable UUID id, @Valid @RequestBody StationRequest request) {
        stationService.update(id, request);
        return ApiResponse.success("站点更新成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        stationService.delete(id);
        return ApiResponse.success("站点删除成功", null);
    }

    @GetMapping("/statistics")
    public ApiResponse<Object> statistics() {
        return ApiResponse.success(stationService.statistics());
    }
}
