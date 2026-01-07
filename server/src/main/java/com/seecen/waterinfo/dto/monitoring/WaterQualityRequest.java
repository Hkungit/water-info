package com.seecen.waterinfo.dto.monitoring;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WaterQualityRequest {

    @NotBlank
    private String stationId;

    private BigDecimal ph;
    private BigDecimal dissolvedOxygen;
    private BigDecimal turbidity;
    private BigDecimal temperature;
    private BigDecimal conductivity;
    private LocalDateTime recordedAt;
}
