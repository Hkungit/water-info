package com.seecen.waterinfo.dto.alarm;

import com.seecen.waterinfo.domain.enums.AlarmSeverity;
import com.seecen.waterinfo.domain.enums.AlarmStatus;
import com.seecen.waterinfo.domain.enums.AlarmType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class AlarmResponse {
    private UUID id;
    private String stationId;
    private String stationName;
    private AlarmType alarmType;
    private AlarmSeverity severity;
    private String message;
    private AlarmStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private ResolvedBy resolvedBy;

    @Data
    @Builder
    public static class ResolvedBy {
        private Long id;
        private String username;
        private String realName;
    }
}
