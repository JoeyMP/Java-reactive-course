package com.react.project.webflux.sec08;

import com.react.project.webflux.sec08.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;

public class ProductsUploadDownloadTest {

    private final Logger log = LoggerFactory.getLogger(ProductsUploadDownloadTest.class);
    private final ProductClient productClient = new ProductClient();

    @Test
    public void upload() {

        Flux<ProductDto> fluxProduct = Flux.range(1, 1_000_000)
                .map(i -> new ProductDto(null, "product-" + i, i));

        this.productClient.uploadProducts(fluxProduct)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void download() {
        this.productClient.downloadProducts()
                .map(ProductDto::toString)
                .as(flux -> FileWriter.create(flux, Path.of("product.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
