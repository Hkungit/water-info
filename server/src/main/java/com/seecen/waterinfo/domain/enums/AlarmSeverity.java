package com.seecen.waterinfo.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AlarmSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static AlarmSeverity fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return AlarmSeverity.valueOf(value.toUpperCase());
    }
}
