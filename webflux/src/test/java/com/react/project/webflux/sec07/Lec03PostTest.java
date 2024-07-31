package com.react.project.webflux.sec07;

import com.react.project.webflux.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec03PostTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void postBodyValue() {
        Product product = new Product(null, "iphone", 123);
        this.client.post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void postBody() {
        var mono = Mono.fromSupplier(() -> new Product(null, "iphone", 123))
                .delayElement(Duration.ofSeconds(2));
        this.client.post()
                .uri("/lec03/product")
                .body(mono, Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
