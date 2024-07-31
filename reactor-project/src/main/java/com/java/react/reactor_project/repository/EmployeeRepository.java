package com.java.react.reactor_project.repository;

import com.java.react.reactor_project.documents.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

    Mono<Employee> findFirstByEmail(String email);
}
