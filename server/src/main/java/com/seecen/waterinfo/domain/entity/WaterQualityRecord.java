package com.seecen.waterinfo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seecen.waterinfo.domain.enums.MonitoringStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("water_quality_records")
public class WaterQualityRecord extends BaseTimeEntity {

    @TableId(value = "id", type = IdType.INPUT)
    private UUID id;

    @TableField("station_id")
    private UUID stationId;

    private BigDecimal ph;

    @TableField("dissolved_oxygen")
    private BigDecimal dissolvedOxygen;

    private BigDecimal turbidity;

    private BigDecimal temperature;

    private BigDecimal conductivity;

    private MonitoringStatus status = MonitoringStatus.NORMAL;

    @TableField("recorded_at")
    private LocalDateTime recordedAt;

    @TableField(exist = false)
    private Station station;
}
