package com.seecen.waterinfo.dto.monitoring;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlowRecordRequest {

    @NotBlank
    private String stationId;

    @NotNull
    private BigDecimal flowRate;

    private BigDecimal velocity;
    private LocalDateTime recordedAt;
}
