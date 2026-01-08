package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.Station;
import com.seecen.waterinfo.domain.enums.StationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {
    long countByStatus(StationStatus status);
}
