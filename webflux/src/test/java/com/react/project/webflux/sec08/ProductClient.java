package com.react.project.webflux.sec08;

import com.react.project.webflux.sec08.dto.ProductDto;
import com.react.project.webflux.sec08.dto.UploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {
    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();

    public Mono<UploadResponse> uploadProducts(Flux<ProductDto> flux) {
        return this.webClient.post()
                .uri("/products/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(flux, ProductDto.class)
                .retrieve()
                .bodyToMono(UploadResponse.class);
    }

    public Flux<ProductDto> downloadProducts() {
        return this.webClient.get()
                .uri("/products/download")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }
}
