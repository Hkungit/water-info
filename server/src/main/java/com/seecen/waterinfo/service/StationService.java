package com.seecen.waterinfo.service;

import com.seecen.waterinfo.common.PageResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seecen.waterinfo.domain.entity.Station;
import com.seecen.waterinfo.domain.enums.StationStatus;
import com.seecen.waterinfo.dto.station.StationRequest;
import com.seecen.waterinfo.dto.station.StationResponse;
import com.seecen.waterinfo.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public PageResponse<StationResponse> list(int page, int size) {
        Page<Station> pageRequest = new Page<>(Math.max(page, 1), size);
        QueryWrapper<Station> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("created_at");
        Page<Station> stations = stationRepository.selectPage(pageRequest, wrapper);
        List<StationResponse> content = stations.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, stations.getTotal(), (int) stations.getPages(), (int) stations.getCurrent(), (int) stations.getSize());
    }

    public StationResponse detail(UUID id) {
        Station station = stationRepository.selectById(id);
        if (station == null) {
            throw new IllegalArgumentException("站点不存在");
        }
        return toResponse(station);
    }

    @Transactional
    public StationResponse create(StationRequest request) {
        Station station = Station.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : StationStatus.ACTIVE)
                .build();
        stationRepository.insert(station);
        return toResponse(station);
    }

    @Transactional
    public void update(UUID id, StationRequest request) {
        Station station = stationRepository.selectById(id);
        if (station == null) {
            throw new IllegalArgumentException("站点不存在");
        }
        station.setName(request.getName());
        station.setLocation(request.getLocation());
        station.setLatitude(request.getLatitude());
        station.setLongitude(request.getLongitude());
        station.setDescription(request.getDescription());
        station.setStatus(request.getStatus() != null ? request.getStatus() : station.getStatus());
        stationRepository.updateById(station);
    }

    @Transactional
    public void delete(UUID id) {
        try {
            int deleted = stationRepository.deleteById(id);
            if (deleted == 0) {
                throw new EmptyResultDataAccessException(1);
            }
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("站点不存在");
        }
    }

    public Map<String, Long> statistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalStations", stationRepository.selectCount(null));
        stats.put("activeStations", stationRepository.selectCount(new QueryWrapper<Station>().eq("status", StationStatus.ACTIVE)));
        stats.put("inactiveStations", stationRepository.selectCount(new QueryWrapper<Station>().eq("status", StationStatus.INACTIVE)));
        stats.put("maintenanceStations", stationRepository.selectCount(new QueryWrapper<Station>().eq("status", StationStatus.MAINTENANCE)));
        return stats;
    }

    private StationResponse toResponse(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .location(station.getLocation())
                .latitude(station.getLatitude())
                .longitude(station.getLongitude())
                .description(station.getDescription())
                .status(station.getStatus())
                .createdAt(station.getCreatedAt())
                .updatedAt(station.getUpdatedAt())
                .build();
    }
}
