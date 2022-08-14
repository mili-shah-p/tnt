
package com.tnt.api.aggregation.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.tnt.api.aggregation.configuration.WebClientWrapper;
import com.tnt.api.aggregation.dtos.Aggregation;

import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class AggregationService {

    private final static Logger logger = LoggerFactory.getLogger(AggregationService.class);
    private final WebClientWrapper webClientWrapper;

    public Flux<Aggregation> getAggregation(final List<String> trackList, final List<String> pricingList, final List<String> shipments) {

        return Flux.zip(getPricing(pricingList), getTrack(trackList), getShipment(shipments))
                                            .parallel()
                                            .runOn(Schedulers.parallel())
                                            .map(r -> {
                                                final Aggregation aggregation = Aggregation.builder()
                                                                            .pricing(r.getT1())
                                                                            .tracking(r.getT2())
                                                                            .shipment(r.getT3())
                                                                            .build();
                                                    return aggregation;
                                                })
                                            .sequential();
    }

    private Mono<Map<String, Float>> getPricing(final List<String> pricing) {
        return webClientWrapper.getWebClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pricing")
                        .queryParam("q", pricing)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Float>>() {})
                .transformDeferred(RateLimiterOperator.of(webClientWrapper.getRateLimiter()));
    }

    private Mono<Map<BigDecimal, String>> getTrack(final List<String> trackList) {
        return webClientWrapper.getWebClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/track")
                        .queryParam("q", trackList)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<BigDecimal, String>>() {})
                .transformDeferred(RateLimiterOperator.of(webClientWrapper.getRateLimiter()));
    }

    private Mono<Map<BigDecimal, List<String>>> getShipment(final List<String> shipments) {
        return webClientWrapper.getWebClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/shipments")
                        .queryParam("q", shipments)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<BigDecimal, List<String>>>() {})
                .transformDeferred(RateLimiterOperator.of(webClientWrapper.getRateLimiter()));
    }
}