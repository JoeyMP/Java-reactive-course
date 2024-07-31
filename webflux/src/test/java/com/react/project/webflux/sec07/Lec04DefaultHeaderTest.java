package com.react.project.webflux.sec07;

import com.react.project.webflux.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec04DefaultHeaderTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> b.defaultHeader("caller-id", "order-service"));

    @Test
    public void defaultHeader() {
        this.client.get()
                .uri("/lec04/product/2")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void overrideHeader() {
        this.client.get()
                .uri("/lec04/product/2")
                .header("caller-id", "updated-value")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void headersWithMap() {
        var map = Map.of(
                "caller-id", "new-value",
                "some-key", "some-value"
        );
        this.client.get()
                .uri("/lec04/product/2")
                .headers(h -> h.setAll(map))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
