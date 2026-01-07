package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.WaterLevelRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WaterLevelRecordRepository extends JpaRepository<WaterLevelRecord, UUID> {
}
