package com.seecen.waterinfo.dto.export;

import lombok.Data;

@Data
public class ExportRequest {
    private String stationId;
    private String startDate;
    private String endDate;
    private String format = "excel";
    private boolean includeChart = false;
    private boolean includeWaterLevel = true;
    private boolean includeFlowMonitoring = true;
    private boolean includeWaterQuality = true;
    private boolean includeAlarms = true;
}
