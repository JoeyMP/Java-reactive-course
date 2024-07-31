package com.java.react.reactor_project.repository;

import com.java.react.reactor_project.documents.Employee;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    private List<Employee> employeeData = new ArrayList<>();

    @BeforeAll
    public void populateData() {
        Employee employeeA = new Employee("TestA", "testA@gmail.com", "11100000");
        Employee employeeB = new Employee("TestB", "testB@gmail.com", "22200000");
        Employee employeeC = new Employee("TestC", "testC@gmail.com", "33300000");
        employeeData.add(employeeA);
        employeeData.add(employeeB);
        employeeData.add(employeeC);

        StepVerifier.create(employeeRepository.insert(employeeA).log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(employeeRepository.save(employeeB).log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(employeeRepository.save(employeeC).log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    //@Test
    //@Order(1)
    public void testGetAll() {
        StepVerifier.create(employeeRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void testFindByEmail() {
        StepVerifier.create(employeeRepository.findFirstByEmail("testB@gmail.com").log())
                .expectSubscription()
                .expectNextMatches(employee -> employee.getEmail().equals("testB@gmail.com"))
                .verifyComplete();
    }

    @Test
    @Order(3)
    public void testUpdateEmployee() {
        Mono<Employee> updatedEmployee = employeeRepository.findFirstByEmail("testB@gmail.com")
                .map(employee -> {
                    employee.setPhone("111111");
                    return employee;
                }).flatMap(employee -> employeeRepository.save(employee));

        StepVerifier.create(updatedEmployee.log())
                .expectSubscription()
                .expectNextMatches(employee -> employee.getPhone().equals("111111"))
                .verifyComplete();
    }

    @Test
    @Order(4)
    public void testDeleteById() {
        Mono<Void> deletedEmployee = employeeRepository.findFirstByEmail("testA@gmail.com")
                .flatMap(employee -> employeeRepository.deleteById(employee.getId()));

        StepVerifier.create(deletedEmployee.log())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @Order(5)
    public void testDeleteEmployee() {
        Mono<Void> deletedEmployee = employeeRepository.findFirstByEmail("testC@gmail.com")
                .flatMap(employee -> employeeRepository.delete(employee));

        StepVerifier.create(deletedEmployee.log())
                .expectSubscription()
                .verifyComplete();
    }

    @AfterAll
    public void clearData() {
        Flux<Employee> data = Flux.fromStream(employeeData.stream().map(Employee::getEmail))
                .flatMap(email -> employeeRepository.findFirstByEmail(email));

        Flux<Void> deleteAll = data.flatMap(e -> employeeRepository.delete(e));
        StepVerifier.create(deleteAll.log())
                .expectSubscription()
                .verifyComplete();
    }
}