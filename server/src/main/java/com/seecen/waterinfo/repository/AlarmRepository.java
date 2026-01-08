package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.Alarm;
import com.seecen.waterinfo.domain.enums.AlarmStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlarmRepository extends JpaRepository<Alarm, UUID> {
    Page<Alarm> findByStatus(AlarmStatus status, Pageable pageable);

    long countByStatus(AlarmStatus status);
}
