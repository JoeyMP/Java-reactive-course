package com.react.project.webflux.sec07;

import com.react.project.webflux.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec07BasicAuthTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> b.defaultHeaders(
            h -> h.setBasicAuth("java", "secret")));

    @Test
    public void basicAuth() {
        this.client.get()
                .uri("/lec07/product/2")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
