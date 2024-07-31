package com.react.project.webflux.sec02;


import com.react.project.webflux.sec02.entity.Customer;
import com.react.project.webflux.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


public class Lesson01CustomerRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lesson01CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAll() {
        Flux<Customer> customerFlux = this.customerRepository.findAll()
                .doOnNext(c -> log.info("{}", c));

        StepVerifier.create(customerFlux.log())
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    public void findById() {
        this.customerRepository.findById(2)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findByName() {
        this.customerRepository.findByName("jake")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findByEmailEndingWith() {
        this.customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void insertAndDeleteCustomer() {
        //insert
        Customer customer = new Customer();
        customer.setName("Joey");
        customer.setEmail("joey.gmail.com");
        this.customerRepository.save(customer)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotNull(c.getId()))
                .expectComplete()
                .verify();

        //count
        this.customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

        this.customerRepository.deleteById(11)
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();
    }

    @Test
    public void updateCustomer() {
        this.customerRepository.findByName("ethan")
                .doOnNext(c -> c.setName("noel"))
                .flatMap(this.customerRepository::save)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("noel", c.getName()))
                .expectComplete()
                .verify();

    }
}
