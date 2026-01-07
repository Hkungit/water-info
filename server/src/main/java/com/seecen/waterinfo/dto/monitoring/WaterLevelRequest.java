package com.seecen.waterinfo.dto.monitoring;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WaterLevelRequest {

    @NotBlank
    private String stationId;

    @NotNull
    private BigDecimal currentLevel;

    private BigDecimal warningLevel;
    private BigDecimal dangerLevel;
    private LocalDateTime recordedAt;
}
