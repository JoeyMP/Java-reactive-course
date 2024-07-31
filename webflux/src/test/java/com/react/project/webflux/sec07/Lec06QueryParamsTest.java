package com.react.project.webflux.sec07;

import com.react.project.webflux.sec07.dto.CalculatorResponse;
import com.react.project.webflux.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec06QueryParamsTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void uriBuilderVariable() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operator}";
        this.client.get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(10, 20, "*"))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void uriBuilderMap() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operator}";
        var map = Map.of(
                "first", 5,
                "second", 6,
                "operator", "+"
        );
        this.client.get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(map))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
