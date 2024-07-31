package com.react.project.webflux.sec09.controller;

import com.react.project.webflux.sec09.dto.ProductDto;
import com.react.project.webflux.sec09.dto.UploadResponse;
import com.react.project.webflux.sec09.service.ProductService;
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

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> mono) {
        return this.productService.saveProduct(mono);
    }

    @GetMapping(value = "stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> productStream(@PathVariable Integer maxPrice) {
        return this.productService.productStream().filter(p -> p.price() <= maxPrice);
    }
}
