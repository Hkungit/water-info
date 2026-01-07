package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.FlowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FlowRecordRepository extends JpaRepository<FlowRecord, UUID> {
}
