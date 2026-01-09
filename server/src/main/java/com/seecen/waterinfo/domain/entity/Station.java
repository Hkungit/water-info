package com.seecen.waterinfo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seecen.waterinfo.domain.enums.StationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("stations")
public class Station extends BaseTimeEntity {

    @TableId(value = "id", type = IdType.INPUT)
    private UUID id;

    private String name;

    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String description;

    private StationStatus status = StationStatus.ACTIVE;
}
