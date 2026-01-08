package com.seecen.waterinfo.service;

import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.domain.entity.Alarm;
import com.seecen.waterinfo.domain.entity.User;
import com.seecen.waterinfo.domain.enums.AlarmSeverity;
import com.seecen.waterinfo.domain.enums.AlarmStatus;
import com.seecen.waterinfo.dto.alarm.AlarmResponse;
import com.seecen.waterinfo.repository.AlarmRepository;
import com.seecen.waterinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public PageResponse<AlarmResponse> list(int page, int size, AlarmStatus status) {
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Alarm> alarms = status != null ? alarmRepository.findByStatus(status, pageRequest) : alarmRepository.findAll(pageRequest);
        List<AlarmResponse> content = alarms.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, alarms.getTotalElements(), alarms.getTotalPages(), alarms.getNumber() + 1, alarms.getSize());
    }

    public long activeCount() {
        return alarmRepository.countByStatus(AlarmStatus.ACTIVE);
    }

    public AlarmResponse detail(UUID id) {
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("报警记录不存在"));
        return toResponse(alarm);
    }

    @Transactional
    public AlarmResponse resolve(UUID id, Long userId) {
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("报警记录不存在"));
        alarm.setStatus(AlarmStatus.RESOLVED);
        alarm.setResolvedAt(LocalDateTime.now());
        if (userId != null) {
            User resolver = userRepository.findById(userId).orElse(null);
            alarm.setResolvedBy(resolver);
        }
        Alarm saved = alarmRepository.save(alarm);
        return toResponse(saved);
    }

    public Map<String, Object> statistics() {
        List<Alarm> alarms = alarmRepository.findAll();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAlarms", alarms.size());
        stats.put("activeAlarms", alarms.stream().filter(a -> a.getStatus() == AlarmStatus.ACTIVE).count());
        stats.put("resolvedAlarms", alarms.stream().filter(a -> a.getStatus() == AlarmStatus.RESOLVED).count());
        stats.put("criticalAlarms", alarms.stream().filter(a -> a.getSeverity() == AlarmSeverity.CRITICAL).count());
        stats.put("highAlarms", alarms.stream().filter(a -> a.getSeverity() == AlarmSeverity.HIGH).count());
        stats.put("mediumAlarms", alarms.stream().filter(a -> a.getSeverity() == AlarmSeverity.MEDIUM).count());
        stats.put("lowAlarms", alarms.stream().filter(a -> a.getSeverity() == AlarmSeverity.LOW).count());
        return stats;
    }

    private AlarmResponse toResponse(Alarm alarm) {
        User resolver = alarm.getResolvedBy();
        return AlarmResponse.builder()
                .id(alarm.getId())
                .stationId(alarm.getStation().getId().toString())
                .stationName(alarm.getStation().getName())
                .alarmType(alarm.getAlarmType())
                .severity(alarm.getSeverity())
                .message(alarm.getMessage())
                .status(alarm.getStatus())
                .createdAt(alarm.getCreatedAt())
                .resolvedAt(alarm.getResolvedAt())
                .resolvedBy(resolver != null ? AlarmResponse.ResolvedBy.builder()
                        .id(resolver.getId())
                        .username(resolver.getUsername())
                        .realName(resolver.getRealName())
                        .build() : null)
                .build();
    }
}
