package com.seecen.waterinfo.service;

import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.domain.enums.AlarmSeverity;
import com.seecen.waterinfo.domain.enums.AlarmStatus;
import com.seecen.waterinfo.domain.enums.AlarmType;
import com.seecen.waterinfo.dto.alarm.AlarmResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AlarmService {

    public PageResponse<AlarmResponse> list(int page, int size) {
        List<AlarmResponse> alarms = sampleAlarms();
        return PageResponse.of(alarms, alarms.size(), 1, page, size);
    }

    public long activeCount() {
        return sampleAlarms().stream().filter(a -> a.getStatus() == AlarmStatus.ACTIVE).count();
    }

    public AlarmResponse detail(UUID id) {
        return sampleAlarms().get(0).toBuilder().id(id).build();
    }

    public AlarmResponse resolve(UUID id) {
        return AlarmResponse.builder()
                .id(id)
                .stationId("550e8400-e29b-41d4-a716-446655440001")
                .stationName("主站监测点")
                .alarmType(AlarmType.WATER_LEVEL_WARNING)
                .severity(AlarmSeverity.HIGH)
                .message("水位超过警戒线，当前水位：15.5m，警戒水位：15.0m")
                .status(AlarmStatus.RESOLVED)
                .createdAt(LocalDateTime.now().minusHours(1))
                .resolvedAt(LocalDateTime.now())
                .resolvedBy(AlarmResponse.ResolvedBy.builder()
                        .id(1L)
                        .username("admin")
                        .realName("系统管理员")
                        .build())
                .build();
    }

    public Object statistics() {
        return new Object() {
            public final int totalAlarms = 50;
            public final int activeAlarms = 5;
            public final int resolvedAlarms = 45;
            public final int criticalAlarms = 2;
            public final int highAlarms = 10;
            public final int mediumAlarms = 25;
            public final int lowAlarms = 13;
        };
    }

    private List<AlarmResponse> sampleAlarms() {
        return List.of(
                AlarmResponse.builder()
                        .id(UUID.fromString("990e8400-e29b-41d4-a716-446655440001"))
                        .stationId("550e8400-e29b-41d4-a716-446655440001")
                        .stationName("主站监测点")
                        .alarmType(AlarmType.WATER_LEVEL_WARNING)
                        .severity(AlarmSeverity.HIGH)
                        .message("水位超过警戒线，当前水位：15.5m，警戒水位：15.0m")
                        .status(AlarmStatus.ACTIVE)
                        .createdAt(LocalDateTime.now().minusHours(1))
                        .build(),
                AlarmResponse.builder()
                        .id(UUID.fromString("990e8400-e29b-41d4-a716-446655440002"))
                        .stationId("550e8400-e29b-41d4-a716-446655440002")
                        .stationName("副站监测点")
                        .alarmType(AlarmType.WATER_QUALITY_ABNORMAL)
                        .severity(AlarmSeverity.MEDIUM)
                        .message("水质异常，pH值：6.2，超出正常范围")
                        .status(AlarmStatus.RESOLVED)
                        .createdAt(LocalDateTime.now().minusHours(3))
                        .resolvedAt(LocalDateTime.now().minusHours(2))
                        .resolvedBy(AlarmResponse.ResolvedBy.builder()
                                .id(2L)
                                .username("operator1")
                                .realName("操作员")
                                .build())
                        .build()
        );
    }
}
