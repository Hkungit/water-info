package com.seecen.waterinfo.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StationStatus {
    ACTIVE,
    INACTIVE,
    MAINTENANCE;

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static StationStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return StationStatus.valueOf(value.toUpperCase());
    }
}
