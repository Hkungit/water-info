package com.seecen.waterinfo.dto.monitoring;

import com.seecen.waterinfo.domain.enums.MonitoringStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FlowRecordResponse {
    private UUID id;
    private String stationId;
    private String stationName;
    private BigDecimal flowRate;
    private BigDecimal velocity;
    private MonitoringStatus status;
    private LocalDateTime recordedAt;
    private LocalDateTime createdAt;
}
