package com.tnt.api.aggregation;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    NEW("NEW"),
    INTRANSIT("IN_TRANSIT"),
    COLLECTING("COLLECTING"),
    COLLECTED("COLLECTED"),
    DELIVERING("DELIVERING"),
    DELIVERED("DELIVERED");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
