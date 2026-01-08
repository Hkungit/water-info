package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.FlowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlowRecordRepository extends JpaRepository<FlowRecord, UUID> {
    Optional<FlowRecord> findTopByOrderByRecordedAtDesc();

    Optional<FlowRecord> findTopByStation_IdOrderByRecordedAtDesc(UUID stationId);

    Page<FlowRecord> findByStation_Id(UUID stationId, Pageable pageable);

    List<FlowRecord> findByStation_Id(UUID stationId, Sort sort);
}
