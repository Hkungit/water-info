package com.seecen.waterinfo.dto.station;

import com.seecen.waterinfo.domain.enums.StationStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StationResponse {
    private UUID id;
    private String name;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private StationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
