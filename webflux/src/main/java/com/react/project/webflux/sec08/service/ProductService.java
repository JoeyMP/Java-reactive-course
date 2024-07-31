package com.react.project.webflux.sec08.service;


import com.react.project.webflux.sec08.dto.ProductDto;
import com.react.project.webflux.sec08.mapper.EntityDtoMapper;
import com.react.project.webflux.sec08.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Flux<ProductDto> saveProduct(Flux<ProductDto> flux) {
        return flux
                .map(EntityDtoMapper::toEntity)
                .as(productRepository::saveAll)
                .map(EntityDtoMapper::toDto);
    }

    public Flux<ProductDto> getAll() {
        return productRepository.findAll().map(EntityDtoMapper::toDto);
    }

    public Mono<Long> getProductsCount() {
        return this.productRepository.count();
    }
}
