package com.seecen.waterinfo.service;

import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.domain.entity.Station;
import com.seecen.waterinfo.domain.enums.StationStatus;
import com.seecen.waterinfo.dto.station.StationRequest;
import com.seecen.waterinfo.dto.station.StationResponse;
import com.seecen.waterinfo.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Station> stations = stationRepository.findAll(pageRequest);
        List<StationResponse> content = stations.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return PageResponse.of(content, stations.getTotalElements(), stations.getTotalPages(), stations.getNumber() + 1, stations.getSize());
    }

    public StationResponse detail(UUID id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("站点不存在"));
        return toResponse(station);
    }

    @Transactional
    public StationResponse create(StationRequest request) {
        Station station = Station.builder()
                .name(request.getName())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : StationStatus.ACTIVE)
                .build();
        Station saved = stationRepository.save(station);
        return toResponse(saved);
    }

    @Transactional
    public void update(UUID id, StationRequest request) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("站点不存在"));
        station.setName(request.getName());
        station.setLocation(request.getLocation());
        station.setLatitude(request.getLatitude());
        station.setLongitude(request.getLongitude());
        station.setDescription(request.getDescription());
        station.setStatus(request.getStatus() != null ? request.getStatus() : station.getStatus());
        stationRepository.save(station);
    }

    @Transactional
    public void delete(UUID id) {
        try {
            stationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("站点不存在");
        }
    }

    public Map<String, Long> statistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalStations", stationRepository.count());
        stats.put("activeStations", stationRepository.countByStatus(StationStatus.ACTIVE));
        stats.put("inactiveStations", stationRepository.countByStatus(StationStatus.INACTIVE));
        stats.put("maintenanceStations", stationRepository.countByStatus(StationStatus.MAINTENANCE));
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
