package com.seecen.waterinfo.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AlarmStatus {
    ACTIVE,
    RESOLVED;

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static AlarmStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return AlarmStatus.valueOf(value.toUpperCase());
    }
}
