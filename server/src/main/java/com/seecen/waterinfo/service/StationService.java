package com.seecen.waterinfo.service;

import com.seecen.waterinfo.common.PageResponse;
import com.seecen.waterinfo.domain.enums.StationStatus;
import com.seecen.waterinfo.dto.station.StationRequest;
import com.seecen.waterinfo.dto.station.StationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class StationService {

    public PageResponse<StationResponse> list(int page, int size) {
        List<StationResponse> stations = Arrays.asList(
                StationResponse.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                        .name("主站监测点")
                        .location("北京市朝阳区")
                        .latitude(BigDecimal.valueOf(39.9042))
                        .longitude(BigDecimal.valueOf(116.4074))
                        .description("主站监测站点")
                        .status(StationStatus.ACTIVE)
                        .createdAt(LocalDateTime.now().minusDays(5))
                        .build(),
                StationResponse.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                        .name("副站监测点")
                        .location("北京市海淀区")
                        .latitude(BigDecimal.valueOf(39.9562))
                        .longitude(BigDecimal.valueOf(116.3105))
                        .description("辅监测站点")
                        .status(StationStatus.ACTIVE)
                        .createdAt(LocalDateTime.now().minusDays(4))
                        .build()
        );
        return PageResponse.of(stations, stations.size(), 1, page, size);
    }

    public StationResponse detail(UUID id) {
        return StationResponse.builder()
                .id(id)
                .name("主站监测点")
                .location("北京市朝阳区")
                .latitude(BigDecimal.valueOf(39.9042))
                .longitude(BigDecimal.valueOf(116.4074))
                .description("主站监测站点")
                .status(StationStatus.ACTIVE)
                .createdAt(LocalDateTime.now().minusDays(5))
                .build();
    }

    public StationResponse create(StationRequest request) {
        return StationResponse.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .description(request.getDescription())
                .status(request.getStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void update(UUID id, StationRequest request) {
        // TODO: implement persistence update
    }

    public void delete(UUID id) {
        // TODO: implement delete
    }

    public Object statistics() {
        return new Object() {
            public final int totalStations = 5;
            public final int activeStations = 4;
            public final int inactiveStations = 0;
            public final int maintenanceStations = 1;
        };
    }
}
