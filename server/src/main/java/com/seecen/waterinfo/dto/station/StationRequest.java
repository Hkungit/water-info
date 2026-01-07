package com.seecen.waterinfo.dto.station;

import com.seecen.waterinfo.domain.enums.StationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StationRequest {

    @NotBlank
    private String name;

    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private StationStatus status = StationStatus.ACTIVE;
}
