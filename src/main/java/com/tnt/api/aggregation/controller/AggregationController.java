package com.tnt.api.aggregation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.api.aggregation.dtos.Aggregation;
import com.tnt.api.aggregation.service.AggregationService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class AggregationController {

	private final AggregationService service;

	@GetMapping(value = "/aggregation")
	public Flux<Aggregation> aggregation(@RequestParam(name = "track") final List<String> trackList, @RequestParam(name = "pricing") final List<String> pricingList, @RequestParam(name = "shipments") final List<String> shipments) {
		return service.getAggregation(trackList, pricingList, shipments);
	}

}
