package com.react.project.webflux.sec05;

import com.react.project.webflux.sec05.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;


@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec05")
public class CustomerServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Test
    public void unauthorized() {
        //no token
        this.webTestClient.get()
                .uri("/customer")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        //invalid token
        this.validateGet("secret",HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void standardCategory(){
        this.validateGet("secret123",HttpStatus.OK);
        this.validatePost("secret123",HttpStatus.FORBIDDEN);
    }

    @Test
    public void primeCategory(){
        this.validateGet("secret456",HttpStatus.OK);
        this.validatePost("secret456",HttpStatus.OK);
    }


    private void validateGet(String token, HttpStatus expectedStatus) {
        this.webTestClient.get()
                .uri("/customer")
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    private void validatePost(String token, HttpStatus expectedStatus) {
        var dto = new CustomerDto(null, "joey", "joey@gmail.com");
        this.webTestClient.post()
                .uri("/customer")
                .bodyValue(dto)
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }


}
