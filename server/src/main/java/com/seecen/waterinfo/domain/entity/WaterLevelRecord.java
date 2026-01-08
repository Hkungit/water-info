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
@TableName("water_level_records")
public class WaterLevelRecord extends BaseTimeEntity {

    @TableId(value = "id", type = IdType.INPUT)
    private UUID id;

    @TableField("station_id")
    private UUID stationId;

    @TableField("current_level")
    private BigDecimal currentLevel;

    @TableField("warning_level")
    private BigDecimal warningLevel;

    @TableField("danger_level")
    private BigDecimal dangerLevel;

    private MonitoringStatus status = MonitoringStatus.NORMAL;

    @TableField("recorded_at")
    private LocalDateTime recordedAt;

    @TableField(exist = false)
    private Station station;
}
