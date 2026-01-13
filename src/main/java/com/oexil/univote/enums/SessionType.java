package com.oexil.univote.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SessionType {
    PHYSICAL("physical"),
    ONLINE("online");

    private final String value;

    SessionType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static SessionType fromString(String value) {
        for (SessionType type : SessionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

