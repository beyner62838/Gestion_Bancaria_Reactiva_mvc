package com.example.Prueba_Tecnica.Entity.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IdentificationType {
    CC("CC"),
    CE("CE"),
    PASSPORT("PASSPORT");

    private final String value;

    IdentificationType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static IdentificationType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (IdentificationType type : IdentificationType.values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid IdentificationType: " + value);
    }
}