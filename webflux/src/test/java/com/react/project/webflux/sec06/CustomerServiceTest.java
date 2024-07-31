package com.react.project.webflux.sec06;

import com.react.project.webflux.sec03.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;


@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec06")
public class CustomerServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Test
    public void allCustomer() {
        this.webTestClient.get()
                .uri("/customer")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .value(list -> log.info("{}", list))
                .hasSize(10);
    }

    @Test
    public void paginatedCustomer() {
        this.webTestClient.get()
                .uri("/customer/paginated?page=3&size=2")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(5)
                .jsonPath("$[1].id").isEqualTo(6);
    }

    @Test
    public void customerById() {
        this.webTestClient.get()
                .uri("/customer/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer() {
        //create
        CustomerDto customer = new CustomerDto(null, "joey", "joey@gmail.com");
        this.webTestClient.post()
                .uri("/customer")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("joey")
                .jsonPath("$.email").isEqualTo("joey@gmail.com");

        //delete
        this.webTestClient.delete()
                .uri("/customer/11")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void updateCustomer() {
        CustomerDto customer = new CustomerDto(null, "noel", "noel@gmail.com");
        this.webTestClient.put()
                .uri("/customer/10")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("noel")
                .jsonPath("$.email").isEqualTo("noel@gmail.com");
    }

    @Test
    public void customerNotFound() {

        //get
        this.webTestClient.get()
                .uri("/customer/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id = 11] is not found");

        //delete
        this.webTestClient.delete()
                .uri("/customer/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id = 11] is not found");

        //put
        this.webTestClient.put()
                .uri("/customer/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id = 11] is not found");
    }

    @Test
    public void invalidInput(){
        //missing name
        var missingName= new CustomerDto(null,null,"joey@gmail.com");
        this.webTestClient.post()
                .uri("/customer")
                .bodyValue(missingName)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Name is required");

        //missing email
        var missingEmail= new CustomerDto(null,"Joey",null);
        this.webTestClient.post()
                .uri("/customer")
                .bodyValue(missingEmail)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required");

        //invalid email
        var invalidEmail= new CustomerDto(null,"Joey","Joey.com");
        this.webTestClient.put()
                .uri("/customer/10")
                .bodyValue(invalidEmail)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required");
    }
}
