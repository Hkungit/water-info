package com.seecen.waterinfo.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
                .map(station -> waterLevelRecordRepository.findTopByStation_IdOrderByRecordedAtDesc(station.getId())
                        .map(record -> toWaterLevelResponse(record, station))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PageResponse<WaterLevelResponse> waterLevelHistory(String stationId, int page, int size) {
        UUID stationUuid = parseUuid(stationId, "站点ID不合法");
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "recordedAt"));
        Page<WaterLevelRecord> records = waterLevelRecordRepository.findByStation_Id(stationUuid, pageRequest);
        List<WaterLevelResponse> content = records.getContent().stream()
                .map(this::toWaterLevelResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, records.getTotalElements(), records.getTotalPages(), records.getNumber() + 1, records.getSize());
    }

    public WaterLevelResponse latestWaterLevelOne() {
        WaterLevelRecord record = waterLevelRecordRepository.findTopByOrderByRecordedAtDesc()
                .orElseThrow(() -> new IllegalArgumentException("暂无水位记录"));
        return toWaterLevelResponse(record);
    }

    @Transactional
    public WaterLevelResponse addWaterLevel(WaterLevelRequest request) {
        Station station = getStation(request.getStationId());
        LocalDateTime recordedAt = request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now();
        MonitoringStatus status = evaluateStatus(request.getCurrentLevel(), request.getWarningLevel(), request.getDangerLevel());
        WaterLevelRecord record = WaterLevelRecord.builder()
                .station(station)
                .currentLevel(request.getCurrentLevel())
                .warningLevel(request.getWarningLevel())
                .dangerLevel(request.getDangerLevel())
                .status(status)
                .recordedAt(recordedAt)
                .build();
        WaterLevelRecord saved = waterLevelRecordRepository.save(record);
        return toWaterLevelResponse(saved, station);
    }

    public Map<String, Object> waterLevelStats(String stationId) {
        List<WaterLevelRecord> records = stationId != null && !stationId.isBlank()
                ? waterLevelRecordRepository.findByStation_Id(parseUuid(stationId, "站点ID不合法"), Sort.by(Sort.Direction.DESC, "recordedAt"))
                : waterLevelRecordRepository.findAll(Sort.by(Sort.Direction.DESC, "recordedAt"));

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
                .map(station -> flowRecordRepository.findTopByStation_IdOrderByRecordedAtDesc(station.getId())
                        .map(record -> toFlowResponse(record, station))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PageResponse<FlowRecordResponse> flowHistory(String stationId, int page, int size) {
        UUID stationUuid = parseUuid(stationId, "站点ID不合法");
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "recordedAt"));
        Page<FlowRecord> records = flowRecordRepository.findByStation_Id(stationUuid, pageRequest);
        List<FlowRecordResponse> content = records.getContent().stream()
                .map(this::toFlowResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, records.getTotalElements(), records.getTotalPages(), records.getNumber() + 1, records.getSize());
    }

    public FlowRecordResponse latestFlowRecordOne() {
        FlowRecord record = flowRecordRepository.findTopByOrderByRecordedAtDesc()
                .orElseThrow(() -> new IllegalArgumentException("暂无流量记录"));
        return toFlowResponse(record);
    }

    @Transactional
    public FlowRecordResponse addFlowRecord(FlowRecordRequest request) {
        Station station = getStation(request.getStationId());
        LocalDateTime recordedAt = request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now();
        FlowRecord record = FlowRecord.builder()
                .station(station)
                .flowRate(request.getFlowRate())
                .velocity(request.getVelocity())
                .status(MonitoringStatus.NORMAL)
                .recordedAt(recordedAt)
                .build();
        FlowRecord saved = flowRecordRepository.save(record);
        return toFlowResponse(saved, station);
    }

    public Map<String, Object> flowStats() {
        List<FlowRecord> records = flowRecordRepository.findAll();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecords", records.size());
        stats.put("normal", records.stream().filter(r -> r.getStatus() == MonitoringStatus.NORMAL).count());
        stats.put("warning", records.stream().filter(r -> r.getStatus() == MonitoringStatus.WARNING).count());
        stats.put("danger", records.stream().filter(r -> r.getStatus() == MonitoringStatus.DANGER).count());
        return stats;
    }

    public List<WaterQualityResponse> latestWaterQuality(String stationId) {
        return resolveStations(stationId).stream()
                .map(station -> waterQualityRecordRepository.findTopByStation_IdOrderByRecordedAtDesc(station.getId())
                        .map(record -> toWaterQualityResponse(record, station))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PageResponse<WaterQualityResponse> waterQualityHistory(String stationId, int page, int size) {
        UUID stationUuid = parseUuid(stationId, "站点ID不合法");
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "recordedAt"));
        Page<WaterQualityRecord> records = waterQualityRecordRepository.findByStation_Id(stationUuid, pageRequest);
        List<WaterQualityResponse> content = records.getContent().stream()
                .map(this::toWaterQualityResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, records.getTotalElements(), records.getTotalPages(), records.getNumber() + 1, records.getSize());
    }

    @Transactional
    public WaterQualityResponse addWaterQuality(WaterQualityRequest request) {
        Station station = getStation(request.getStationId());
        LocalDateTime recordedAt = request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now();
        WaterQualityRecord record = WaterQualityRecord.builder()
                .station(station)
                .ph(request.getPh())
                .dissolvedOxygen(request.getDissolvedOxygen())
                .turbidity(request.getTurbidity())
                .temperature(request.getTemperature())
                .conductivity(request.getConductivity())
                .status(MonitoringStatus.NORMAL)
                .recordedAt(recordedAt)
                .build();
        WaterQualityRecord saved = waterQualityRecordRepository.save(record);
        return toWaterQualityResponse(saved, station);
    }

    private List<Station> resolveStations(String stationId) {
        if (stationId != null && !stationId.isBlank()) {
            return List.of(getStation(stationId));
        }
        return stationRepository.findAll();
    }

    private Station getStation(String stationId) {
        UUID stationUuid = parseUuid(stationId, "站点ID不合法");
        return stationRepository.findById(stationUuid)
                .orElseThrow(() -> new IllegalArgumentException("监测站点不存在"));
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
        return toWaterLevelResponse(record, record.getStation());
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
        return toFlowResponse(record, record.getStation());
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
        return toWaterQualityResponse(record, record.getStation());
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
