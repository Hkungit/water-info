package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.WaterQualityRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WaterQualityRecordRepository extends JpaRepository<WaterQualityRecord, UUID> {
    Optional<WaterQualityRecord> findTopByOrderByRecordedAtDesc();

    Optional<WaterQualityRecord> findTopByStation_IdOrderByRecordedAtDesc(UUID stationId);

    Page<WaterQualityRecord> findByStation_Id(UUID stationId, Pageable pageable);

    List<WaterQualityRecord> findByStation_Id(UUID stationId, Sort sort);
}
