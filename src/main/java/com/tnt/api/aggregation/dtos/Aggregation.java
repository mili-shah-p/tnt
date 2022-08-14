package com.tnt.api.aggregation.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Aggregation {

    private final Map<String, Float> pricing;

    private final Map<BigDecimal, String> tracking;

    private final Map<BigDecimal, List<String>> shipment;
}