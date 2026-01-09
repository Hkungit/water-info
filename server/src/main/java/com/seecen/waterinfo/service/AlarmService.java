package com.seecen.waterinfo.service;

import com.seecen.waterinfo.common.PageResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seecen.waterinfo.domain.entity.Alarm;
import com.seecen.waterinfo.domain.entity.Station;
import com.seecen.waterinfo.domain.entity.User;
import com.seecen.waterinfo.domain.enums.AlarmSeverity;
import com.seecen.waterinfo.domain.enums.AlarmStatus;
import com.seecen.waterinfo.dto.alarm.AlarmResponse;
import com.seecen.waterinfo.repository.AlarmRepository;
import com.seecen.waterinfo.repository.StationRepository;
import com.seecen.waterinfo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;

    public PageResponse<AlarmResponse> list(int page, int size, AlarmStatus status) {
        Page<Alarm> pageRequest = new Page<>(Math.max(page, 1), size);
        QueryWrapper<Alarm> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("created_at");
        Page<Alarm> alarms = alarmRepository.selectPage(pageRequest, wrapper);
        List<Alarm> records = alarms.getRecords();
        attachRelations(records);
        List<AlarmResponse> content = records.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, alarms.getTotal(), (int) alarms.getPages(), (int) alarms.getCurrent(), (int) alarms.getSize());
    }

    public long activeCount() {
        return alarmRepository.selectCount(new QueryWrapper<Alarm>().eq("status", AlarmStatus.ACTIVE));
    }

    public AlarmResponse detail(UUID id) {
        Alarm alarm = alarmRepository.selectById(id);
        if (alarm == null) {
            throw new IllegalArgumentException("报警记录不存在");
        }
        attachRelations(List.of(alarm));
        return toResponse(alarm);
    }

    @Transactional
    public AlarmResponse resolve(UUID id, Long userId) {
        Alarm alarm = alarmRepository.selectById(id);
        if (alarm == null) {
            throw new IllegalArgumentException("报警记录不存在");
        }
        alarm.setStatus(AlarmStatus.RESOLVED);
        alarm.setResolvedAt(LocalDateTime.now());
        if (userId != null) {
            alarm.setResolvedById(userId);
        }
        alarmRepository.updateById(alarm);
        attachRelations(List.of(alarm));
        return toResponse(alarm);
    }

    public Map<String, Object> statistics() {
        List<Alarm> alarms = alarmRepository.selectList(null);
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

    private void attachRelations(List<Alarm> alarms) {
        if (alarms == null || alarms.isEmpty()) {
            return;
        }
        Map<UUID, Station> stationMap = stationRepository.selectBatchIds(
                        alarms.stream()
                                .map(Alarm::getStationId)
                                .filter(Objects::nonNull)
                                .distinct()
                                .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
        Map<Long, User> userMap = userRepository.selectBatchIds(
                        alarms.stream()
                                .map(Alarm::getResolvedById)
                                .filter(Objects::nonNull)
                                .distinct()
                                .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        alarms.forEach(alarm -> {
            alarm.setStation(stationMap.get(alarm.getStationId()));
            alarm.setResolvedBy(userMap.get(alarm.getResolvedById()));
        });
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
