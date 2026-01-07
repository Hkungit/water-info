package com.seecen.waterinfo.service;

import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.domain.enums.MonitoringStatus;
import com.seecen.waterinfo.dto.monitoring.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class MonitoringService {

    public List<WaterLevelResponse> latestWaterLevels(String stationId) {
        return Arrays.asList(
                WaterLevelResponse.builder()
                        .id(UUID.fromString("660e8400-e29b-41d4-a716-446655440001"))
                        .stationId("550e8400-e29b-41d4-a716-446655440001")
                        .stationName("主站监测点")
                        .currentLevel(BigDecimal.valueOf(12.50))
                        .warningLevel(BigDecimal.valueOf(15.00))
                        .dangerLevel(BigDecimal.valueOf(18.00))
                        .status(MonitoringStatus.NORMAL)
                        .recordedAt(LocalDateTime.now().minusMinutes(10))
                        .createdAt(LocalDateTime.now().minusMinutes(10))
                        .build(),
                WaterLevelResponse.builder()
                        .id(UUID.fromString("660e8400-e29b-41d4-a716-446655440002"))
                        .stationId("550e8400-e29b-41d4-a716-446655440002")
                        .stationName("副站监测点")
                        .currentLevel(BigDecimal.valueOf(10.20))
                        .warningLevel(BigDecimal.valueOf(14.00))
                        .dangerLevel(BigDecimal.valueOf(17.00))
                        .status(MonitoringStatus.NORMAL)
                        .recordedAt(LocalDateTime.now().minusMinutes(15))
                        .createdAt(LocalDateTime.now().minusMinutes(15))
                        .build()
        );
    }

    public PageResponse<WaterLevelResponse> waterLevelHistory(String stationId, int page, int size) {
        List<WaterLevelResponse> data = latestWaterLevels(stationId);
        return PageResponse.of(data, data.size(), 1, page, size);
    }

    public WaterLevelResponse latestWaterLevelOne() {
        return latestWaterLevels(null).get(0);
    }

    public WaterLevelResponse addWaterLevel(WaterLevelRequest request) {
        return WaterLevelResponse.builder()
                .id(UUID.randomUUID())
                .stationId(request.getStationId())
                .stationName("新建站点")
                .currentLevel(request.getCurrentLevel())
                .warningLevel(request.getWarningLevel())
                .dangerLevel(request.getDangerLevel())
                .status(MonitoringStatus.NORMAL)
                .recordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Object waterLevelStats() {
        return new Object() {
            public final int totalRecords = 2;
            public final int normal = 2;
            public final int warning = 0;
            public final int danger = 0;
        };
    }

    public List<FlowRecordResponse> latestFlowRecords(String stationId) {
        return List.of(
                FlowRecordResponse.builder()
                        .id(UUID.fromString("770e8400-e29b-41d4-a716-446655440001"))
                        .stationId("550e8400-e29b-41d4-a716-446655440001")
                        .stationName("主站监测点")
                        .flowRate(BigDecimal.valueOf(150.25))
                        .velocity(BigDecimal.valueOf(2.5))
                        .status(MonitoringStatus.NORMAL)
                        .recordedAt(LocalDateTime.now().minusMinutes(5))
                        .createdAt(LocalDateTime.now().minusMinutes(5))
                        .build()
        );
    }

    public PageResponse<FlowRecordResponse> flowHistory(String stationId, int page, int size) {
        List<FlowRecordResponse> data = latestFlowRecords(stationId);
        return PageResponse.of(data, data.size(), 1, page, size);
    }

    public FlowRecordResponse latestFlowRecordOne() {
        return latestFlowRecords(null).get(0);
    }

    public FlowRecordResponse addFlowRecord(FlowRecordRequest request) {
        return FlowRecordResponse.builder()
                .id(UUID.randomUUID())
                .stationId(request.getStationId())
                .stationName("新建站点")
                .flowRate(request.getFlowRate())
                .velocity(request.getVelocity())
                .status(MonitoringStatus.NORMAL)
                .recordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Object flowStats() {
        return new Object() {
            public final int totalRecords = 1;
            public final int normal = 1;
            public final int warning = 0;
            public final int danger = 0;
        };
    }

    public List<WaterQualityResponse> latestWaterQuality(String stationId) {
        return List.of(
                WaterQualityResponse.builder()
                        .id(UUID.fromString("880e8400-e29b-41d4-a716-446655440001"))
                        .stationId("550e8400-e29b-41d4-a716-446655440001")
                        .stationName("主站监测点")
                        .ph(BigDecimal.valueOf(7.2))
                        .dissolvedOxygen(BigDecimal.valueOf(8.5))
                        .turbidity(BigDecimal.valueOf(3.2))
                        .temperature(BigDecimal.valueOf(22.5))
                        .conductivity(BigDecimal.valueOf(450.0))
                        .status(MonitoringStatus.NORMAL)
                        .recordedAt(LocalDateTime.now().minusMinutes(20))
                        .createdAt(LocalDateTime.now().minusMinutes(20))
                        .build()
        );
    }

    public PageResponse<WaterQualityResponse> waterQualityHistory(String stationId, int page, int size) {
        List<WaterQualityResponse> data = latestWaterQuality(stationId);
        return PageResponse.of(data, data.size(), 1, page, size);
    }

    public WaterQualityResponse addWaterQuality(WaterQualityRequest request) {
        return WaterQualityResponse.builder()
                .id(UUID.randomUUID())
                .stationId(request.getStationId())
                .stationName("新建站点")
                .ph(request.getPh())
                .dissolvedOxygen(request.getDissolvedOxygen())
                .turbidity(request.getTurbidity())
                .temperature(request.getTemperature())
                .conductivity(request.getConductivity())
                .status(MonitoringStatus.NORMAL)
                .recordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
