package com.seecen.waterinfo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seecen.waterinfo.domain.enums.AlarmSeverity;
import com.seecen.waterinfo.domain.enums.AlarmStatus;
import com.seecen.waterinfo.domain.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarms")
public class Alarm extends BaseTimeEntity {

    @TableId(value = "id", type = IdType.INPUT)
    private UUID id;

    @TableField("station_id")
    private UUID stationId;

    @TableField("alarm_type")
    private AlarmType alarmType;

    private AlarmSeverity severity;

    private String message;

    private AlarmStatus status = AlarmStatus.ACTIVE;

    @TableField("resolved_at")
    private LocalDateTime resolvedAt;

    @TableField("resolved_by")
    private Long resolvedById;

    @TableField(exist = false)
    private Station station;

    @TableField(exist = false)
    private User resolvedBy;
}
