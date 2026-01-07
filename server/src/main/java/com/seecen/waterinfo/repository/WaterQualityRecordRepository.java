package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.WaterQualityRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WaterQualityRecordRepository extends JpaRepository<WaterQualityRecord, UUID> {
}
