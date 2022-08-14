package com.tnt.api.aggregation.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class WebClientWrapper {

    @Value("${base.url}")
    private String baseUrl;

    public WebClient getWebClient() {
            return WebClient.builder()
                    .baseUrl(baseUrl)
                    .exchangeStrategies(ExchangeStrategies.builder().codecs(this::acceptedCodecs).build())
                    .build();

    }

    public RateLimiter getRateLimiter() {
        return RateLimiter.of("my-rate-limiter",
                RateLimiterConfig.custom()
                        .limitRefreshPeriod(Duration.ofSeconds(5))
                        .limitForPeriod(5)
                        .timeoutDuration(Duration.ofSeconds(15))
                        .build());
    }

    private void acceptedCodecs(final ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.customCodecs().registerWithDefaultConfig(new Jackson2JsonDecoder(new ObjectMapper(), MediaType.APPLICATION_JSON));
        clientCodecConfigurer.customCodecs().registerWithDefaultConfig(new Jackson2JsonEncoder(new ObjectMapper(), MediaType.APPLICATION_JSON));
    }
}