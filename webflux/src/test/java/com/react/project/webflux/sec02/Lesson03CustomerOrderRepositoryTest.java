package com.react.project.webflux.sec02;


import com.react.project.webflux.sec02.repository.CustomerOrderRepository;
import com.react.project.webflux.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;


public class Lesson03CustomerOrderRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lesson03CustomerOrderRepositoryTest.class);

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Test
    public void getProductsOrderedByCustomer() {
        this.customerOrderRepository.getProductsOrderedByCustomer("jake")
                .doOnNext(d -> log.info("{}", d))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void getOrderDetailByProduct() {
        this.customerOrderRepository.getOrderDetailByProduct("iphone 18")
                .doOnNext(d -> log.info("{}", d))
                .as(StepVerifier::create)
                .assertNext(d -> Assertions.assertEquals(850, d.amount()))
                .assertNext(d -> Assertions.assertEquals(750, d.amount()))
                .expectComplete()
                .verify();
    }

}
