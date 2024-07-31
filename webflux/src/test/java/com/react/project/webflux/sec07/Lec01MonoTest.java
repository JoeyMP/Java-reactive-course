package com.react.project.webflux.sec07;

import com.react.project.webflux.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class Lec01MonoTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void simpleGet() throws InterruptedException {
        this.client.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

        Thread.sleep(2000);
    }

    @Test
    public void concurrencyRequest() throws InterruptedException {
        for (int i = 0; i <= 100; i++) {
            this.client.get()
                    .uri("/lec01/product/{id}", i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .subscribe();
        }
        Thread.sleep(2000);
    }
}
