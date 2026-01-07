package com.seecen.waterinfo.domain.entity;

import com.seecen.waterinfo.domain.enums.MonitoringStatus;
import jakarta.persistence.*;
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
@Entity
@Table(name = "water_quality_records")
public class WaterQualityRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(precision = 10, scale = 2)
    private BigDecimal ph;

    @Column(name = "dissolved_oxygen", precision = 10, scale = 2)
    private BigDecimal dissolvedOxygen;

    @Column(precision = 10, scale = 2)
    private BigDecimal turbidity;

    @Column(precision = 10, scale = 2)
    private BigDecimal temperature;

    @Column(precision = 10, scale = 2)
    private BigDecimal conductivity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private MonitoringStatus status = MonitoringStatus.NORMAL;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}
