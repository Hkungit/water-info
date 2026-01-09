package com.seecen.waterinfo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.domain.entity.FlowRecord;
import com.seecen.waterinfo.domain.entity.Station;
import com.seecen.waterinfo.domain.entity.WaterLevelRecord;
import com.seecen.waterinfo.domain.entity.WaterQualityRecord;
import com.seecen.waterinfo.domain.enums.MonitoringStatus;
import com.seecen.waterinfo.dto.monitoring.FlowRecordRequest;
import com.seecen.waterinfo.dto.monitoring.FlowRecordResponse;
import com.seecen.waterinfo.dto.monitoring.WaterLevelRequest;
import com.seecen.waterinfo.dto.monitoring.WaterLevelResponse;
import com.seecen.waterinfo.dto.monitoring.WaterQualityRequest;
import com.seecen.waterinfo.dto.monitoring.WaterQualityResponse;
import com.seecen.waterinfo.repository.FlowRecordRepository;
import com.seecen.waterinfo.repository.StationRepository;
import com.seecen.waterinfo.repository.WaterLevelRecordRepository;
import com.seecen.waterinfo.repository.WaterQualityRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonitoringService {

    private final WaterLevelRecordRepository waterLevelRecordRepository;
    private final FlowRecordRepository flowRecordRepository;
    private final WaterQualityRecordRepository waterQualityRecordRepository;
    private final StationRepository stationRepository;

    public List<WaterLevelResponse> latestWaterLevels(String stationId) {
        return resolveStations(stationId).stream()
                .map(station -> {
                    WaterLevelRecord record = waterLevelRecordRepository.selectOne(new QueryWrapper<WaterLevelRecord>()
                            .eq("station_id", station.getId())
                            .orderByDesc("recorded_at")
                            .last("LIMIT 1"));
                    return record != null ? toWaterLevelResponse(record, station) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PageResponse<WaterLevelResponse> waterLevelHistory(String stationId, int page, int size) {
        UUID stationUuid = parseUuid(stationId, "站点ID不合法");
        Page<WaterLevelRecord> pageRequest = new Page<>(Math.max(page, 1), size);
        QueryWrapper<WaterLevelRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("station_id", stationUuid).orderByDesc("recorded_at");
        Page<WaterLevelRecord> records = waterLevelRecordRepository.selectPage(pageRequest, wrapper);
        Station station = getStation(stationId);
        List<WaterLevelResponse> content = records.getRecords().stream()
                .map(record -> toWaterLevelResponse(record, station))
                .collect(Collectors.toList());
        return PageResponse.of(content, records.getTotal(), (int) records.getPages(), (int) records.getCurrent(), (int) records.getSize());
    }

    public WaterLevelResponse latestWaterLevelOne() {
        WaterLevelRecord record = waterLevelRecordRepository.selectOne(new QueryWrapper<WaterLevelRecord>()
                .orderByDesc("recorded_at")
                .last("LIMIT 1"));
        if (record == null) {
            throw new IllegalArgumentException("暂无水位记录");
        }
        Station station = getStation(record.getStationId().toString());
        return toWaterLevelResponse(record, station);
    }

    @Transactional
    public WaterLevelResponse addWaterLevel(WaterLevelRequest request) {
        Station station = getStation(request.getStationId());
        LocalDateTime recordedAt = request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now();
        MonitoringStatus status = evaluateStatus(request.getCurrentLevel(), request.getWarningLevel(), request.getDangerLevel());
        WaterLevelRecord record = WaterLevelRecord.builder()
                .id(UUID.randomUUID())
                .stationId(station.getId())
                .currentLevel(request.getCurrentLevel())
                .warningLevel(request.getWarningLevel())
                .dangerLevel(request.getDangerLevel())
                .status(status)
                .recordedAt(recordedAt)
                .build();
        waterLevelRecordRepository.insert(record);
        return toWaterLevelResponse(record, station);
    }

    public Map<String, Object> waterLevelStats(String stationId) {
        List<WaterLevelRecord> records = stationId != null && !stationId.isBlank()
                ? waterLevelRecordRepository.selectList(new QueryWrapper<WaterLevelRecord>()
                .eq("station_id", parseUuid(stationId, "站点ID不合法"))
                .orderByDesc("recorded_at"))
                : waterLevelRecordRepository.selectList(new QueryWrapper<WaterLevelRecord>().orderByDesc("recorded_at"));

        Map<String, Object> stats = new HashMap<>();
        if (records.isEmpty()) {
            stats.put("avgLevel", 0);
            stats.put("maxLevel", 0);
            stats.put("minLevel", 0);
            stats.put("normalCount", 0);
            stats.put("warningCount", 0);
            stats.put("dangerCount", 0);
            stats.put("totalRecords", 0);
            return stats;
        }

        BigDecimal max = records.stream().map(WaterLevelRecord::getCurrentLevel).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal min = records.stream().map(WaterLevelRecord::getCurrentLevel).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal sum = records.stream().map(WaterLevelRecord::getCurrentLevel).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = sum.divide(BigDecimal.valueOf(records.size()), 2, RoundingMode.HALF_UP);

        stats.put("avgLevel", avg);
        stats.put("maxLevel", max);
        stats.put("minLevel", min);
        stats.put("normalCount", records.stream().filter(r -> r.getStatus() == MonitoringStatus.NORMAL).count());
        stats.put("warningCount", records.stream().filter(r -> r.getStatus() == MonitoringStatus.WARNING).count());
        stats.put("dangerCount", records.stream().filter(r -> r.getStatus() == MonitoringStatus.DANGER).count());
        stats.put("totalRecords", records.size());
        return stats;
    }

    public List<FlowRecordResponse> latestFlowRecords(String stationId) {
        return resolveStations(stationId).stream()
                .map(station -> {
                    FlowRecord record = flowRecordRepository.selectOne(new QueryWrapper<FlowRecord>()
                            .eq("station_id", station.getId())
                            .orderByDesc("recorded_at")
                            .last("LIMIT 1"));
                    return record != null ? toFlowResponse(record, station) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PageResponse<FlowRecordResponse> flowHistory(String stationId, int page, int size) {
        UUID stationUuid = parseUuid(stationId, "站点ID不合法");
        Page<FlowRecord> pageRequest = new Page<>(Math.max(page, 1), size);
        QueryWrapper<FlowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("station_id", stationUuid).orderByDesc("recorded_at");
        Page<FlowRecord> records = flowRecordRepository.selectPage(pageRequest, wrapper);
        Station station = getStation(stationId);
        List<FlowRecordResponse> content = records.getRecords().stream()
                .map(record -> toFlowResponse(record, station))
                .collect(Collectors.toList());
        return PageResponse.of(content, records.getTotal(), (int) records.getPages(), (int) records.getCurrent(), (int) records.getSize());
    }

    public FlowRecordResponse latestFlowRecordOne() {
        FlowRecord record = flowRecordRepository.selectOne(new QueryWrapper<FlowRecord>()
                .orderByDesc("recorded_at")
                .last("LIMIT 1"));
        if (record == null) {
            throw new IllegalArgumentException("暂无流量记录");
        }
        Station station = getStation(record.getStationId().toString());
        return toFlowResponse(record, station);
    }

    @Transactional
    public FlowRecordResponse addFlowRecord(FlowRecordRequest request) {
        Station station = getStation(request.getStationId());
        LocalDateTime recordedAt = request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now();
        FlowRecord record = FlowRecord.builder()
                .id(UUID.randomUUID())
                .stationId(station.getId())
                .flowRate(request.getFlowRate())
                .velocity(request.getVelocity())
                .status(MonitoringStatus.NORMAL)
                .recordedAt(recordedAt)
                .build();
        flowRecordRepository.insert(record);
        return toFlowResponse(record, station);
    }

    public Map<String, Object> flowStats() {
        List<FlowRecord> records = flowRecordRepository.selectList(null);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", records.size());
        stats.put("normal", records.stream().filter(r -> r.getStatus() == MonitoringStatus.NORMAL).count());
        stats.put("warning", records.stream().filter(r -> r.getStatus() == MonitoringStatus.WARNING).count());
        stats.put("danger", records.stream().filter(r -> r.getStatus() == MonitoringStatus.DANGER).count());
        return stats;
    }

    public List<WaterQualityResponse> latestWaterQuality(String stationId) {
        return resolveStations(stationId).stream()
                .map(station -> {
                    WaterQualityRecord record = waterQualityRecordRepository.selectOne(new QueryWrapper<WaterQualityRecord>()
                            .eq("station_id", station.getId())
                            .orderByDesc("recorded_at")
                            .last("LIMIT 1"));
                    return record != null ? toWaterQualityResponse(record, station) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PageResponse<WaterQualityResponse> waterQualityHistory(String stationId, int page, int size) {
        UUID stationUuid = parseUuid(stationId, "站点ID不合法");
        Page<WaterQualityRecord> pageRequest = new Page<>(Math.max(page, 1), size);
        QueryWrapper<WaterQualityRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("station_id", stationUuid).orderByDesc("recorded_at");
        Page<WaterQualityRecord> records = waterQualityRecordRepository.selectPage(pageRequest, wrapper);
        Station station = getStation(stationId);
        List<WaterQualityResponse> content = records.getRecords().stream()
                .map(record -> toWaterQualityResponse(record, station))
                .collect(Collectors.toList());
        return PageResponse.of(content, records.getTotal(), (int) records.getPages(), (int) records.getCurrent(), (int) records.getSize());
    }

    @Transactional
    public WaterQualityResponse addWaterQuality(WaterQualityRequest request) {
        Station station = getStation(request.getStationId());
        LocalDateTime recordedAt = request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now();
        WaterQualityRecord record = WaterQualityRecord.builder()
                .id(UUID.randomUUID())
                .stationId(station.getId())
                .ph(request.getPh())
                .dissolvedOxygen(request.getDissolvedOxygen())
                .turbidity(request.getTurbidity())
                .temperature(request.getTemperature())
                .conductivity(request.getConductivity())
                .status(MonitoringStatus.NORMAL)
                .recordedAt(recordedAt)
                .build();
        waterQualityRecordRepository.insert(record);
        return toWaterQualityResponse(record, station);
    }

    private List<Station> resolveStations(String stationId) {
        if (stationId != null && !stationId.isBlank()) {
            return List.of(getStation(stationId));
        }
        return stationRepository.selectList(null);
    }

    private Station getStation(String stationId) {
        UUID stationUuid = parseUuid(stationId, "站点ID不合法");
        Station station = stationRepository.selectById(stationUuid);
        if (station == null) {
            throw new IllegalArgumentException("监测站点不存在");
        }
        return station;
    }

    private UUID parseUuid(String id, String message) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            throw new IllegalArgumentException(message);
        }
    }

    private MonitoringStatus evaluateStatus(BigDecimal current, BigDecimal warning, BigDecimal danger) {
        if (danger != null && current.compareTo(danger) >= 0) {
            return MonitoringStatus.DANGER;
        }
        if (warning != null && current.compareTo(warning) >= 0) {
            return MonitoringStatus.WARNING;
        }
        return MonitoringStatus.NORMAL;
    }

    private WaterLevelResponse toWaterLevelResponse(WaterLevelRecord record) {
        Station station = getStation(record.getStationId().toString());
        return toWaterLevelResponse(record, station);
    }

    private WaterLevelResponse toWaterLevelResponse(WaterLevelRecord record, Station station) {
        return WaterLevelResponse.builder()
                .id(record.getId())
                .stationId(station.getId().toString())
                .stationName(station.getName())
                .currentLevel(record.getCurrentLevel())
                .warningLevel(record.getWarningLevel())
                .dangerLevel(record.getDangerLevel())
                .status(record.getStatus())
                .recordedAt(record.getRecordedAt())
                .createdAt(record.getCreatedAt())
                .build();
    }

    private FlowRecordResponse toFlowResponse(FlowRecord record) {
        Station station = getStation(record.getStationId().toString());
        return toFlowResponse(record, station);
    }

    private FlowRecordResponse toFlowResponse(FlowRecord record, Station station) {
        return FlowRecordResponse.builder()
                .id(record.getId())
                .stationId(station.getId().toString())
                .stationName(station.getName())
                .flowRate(record.getFlowRate())
                .velocity(record.getVelocity())
                .status(record.getStatus())
                .recordedAt(record.getRecordedAt())
                .createdAt(record.getCreatedAt())
                .build();
    }

    private WaterQualityResponse toWaterQualityResponse(WaterQualityRecord record) {
        Station station = getStation(record.getStationId().toString());
        return toWaterQualityResponse(record, station);
    }

    private WaterQualityResponse toWaterQualityResponse(WaterQualityRecord record, Station station) {
        return WaterQualityResponse.builder()
                .id(record.getId())
                .stationId(station.getId().toString())
                .stationName(station.getName())
                .ph(record.getPh())
                .dissolvedOxygen(record.getDissolvedOxygen())
                .turbidity(record.getTurbidity())
                .temperature(record.getTemperature())
                .conductivity(record.getConductivity())
                .status(record.getStatus())
                .recordedAt(record.getRecordedAt())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
