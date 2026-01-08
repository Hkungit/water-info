package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.WaterLevelRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WaterLevelRecordRepository extends JpaRepository<WaterLevelRecord, UUID> {
    Optional<WaterLevelRecord> findTopByOrderByRecordedAtDesc();

    Optional<WaterLevelRecord> findTopByStation_IdOrderByRecordedAtDesc(UUID stationId);

    Page<WaterLevelRecord> findByStation_Id(UUID stationId, Pageable pageable);

    List<WaterLevelRecord> findByStation_Id(UUID stationId, Sort sort);
}
