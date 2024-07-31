package com.java.react.reactor_project.controller;

import com.java.react.reactor_project.documents.Employee;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private Employee savedEmployee;

    @Test
    @Order(0)
    public void testSaveEmployee() {
        Employee requestBody = new Employee("Test", "test@gmail.com", "999999");
        Flux<Employee> result = webTestClient.post()
                .uri("/api/v1/employee")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isAccepted()
                .returnResult(Employee.class).getResponseBody()
                .log();

        result.next().subscribe(employee -> {
            this.savedEmployee = employee;
        });

        Assertions.assertNotNull(savedEmployee);
    }

    @Test
    @Order(1)
    public void testFindByEmail() {
        Flux<Employee> employee = webTestClient.get()
                .uri("/api/v1/employee/email/{email}", "test@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Employee.class).getResponseBody()
                .log();

        StepVerifier.create(employee)
                .expectSubscription()
                .expectNextMatches(e -> e.getEmail().equals("test@gmail.com"))
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void testUpdateEmployee() {
        Employee requestBody = new Employee("Test", "test-update@gmail.com", "999999");
        Flux<Employee> result = webTestClient.put()
                .uri("/api/v1/employee/{id}", savedEmployee.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isAccepted()
                .returnResult(Employee.class).getResponseBody()
                .log();

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(e -> e.getEmail().equals("test-update@gmail.com"))
                .verifyComplete();
    }

    //@Test
    //@Order(3)
    public void testFindAll() {
        Flux<Employee> result = webTestClient.get()
                .uri("/api/v1/employee")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Employee.class).getResponseBody()
                .log();

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(4)
    public void testDelete() {
        Flux<Void> result = webTestClient.delete()
                .uri("/api/v1/employee/{id}", savedEmployee.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Void.class).getResponseBody()
                .log();

        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();
    }

}