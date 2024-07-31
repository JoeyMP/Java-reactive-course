package com.react.project.webflux.sec08.controller;

import com.react.project.webflux.sec08.dto.ProductDto;
import com.react.project.webflux.sec08.dto.UploadResponse;
import com.react.project.webflux.sec08.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProduct(@RequestBody Flux<ProductDto> flux) {
        log.info("invoked");
        return this.productService.saveProduct(flux)
                .then(this.productService.getProductsCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> downloadProduct() {
        log.info("invoked");
        return this.productService.getAll();
    }
}
