package com.seecen.waterinfo.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MonitoringStatus {
    NORMAL,
    WARNING,
    DANGER;

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static MonitoringStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return MonitoringStatus.valueOf(value.toUpperCase());
    }
}
