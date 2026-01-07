package com.seecen.waterinfo.repository;

import com.seecen.waterinfo.domain.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlarmRepository extends JpaRepository<Alarm, UUID> {
}
