package com.seecen.waterinfo.service;

import com.seecen.waterinfo.dto.export.ExportRequest;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

    public String exportWaterLevels(ExportRequest request) {
        return "water-levels-" + request.getFormat();
    }

    public String exportFlowMonitoring(ExportRequest request) {
        return "flow-monitoring-" + request.getFormat();
    }

    public String exportWaterQuality(ExportRequest request) {
        return "water-quality-" + request.getFormat();
    }

    public String exportAlarms(ExportRequest request) {
        return "alarms-" + request.getFormat();
    }

    public String exportReport(ExportRequest request) {
        return "report-" + request.getFormat();
    }
}
