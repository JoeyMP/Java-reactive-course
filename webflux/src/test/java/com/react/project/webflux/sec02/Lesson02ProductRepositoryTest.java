package com.react.project.webflux.sec02;


import com.react.project.webflux.sec02.entity.Customer;
import com.react.project.webflux.sec02.repository.CustomerRepository;
import com.react.project.webflux.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


public class Lesson02ProductRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lesson02ProductRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void findByRangePrice() {
        this.productRepository.findByPriceBetween(1000, 2000)
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    public void pageable() {
        this.productRepository.findBy(PageRequest.of(0, 3).withSort(Sort.by("price").ascending()))
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals(200, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(250, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(300, p.getPrice()))
                .expectComplete()
                .verify();
    }


}
