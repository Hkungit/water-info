package com.seecen.waterinfo.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AlarmType {
    WATER_LEVEL_WARNING,
    WATER_LEVEL_DANGER,
    WATER_QUALITY_ABNORMAL,
    FLOW_ABNORMAL,
    EQUIPMENT_FAILURE;

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static AlarmType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return AlarmType.valueOf(value.toUpperCase());
    }
}
