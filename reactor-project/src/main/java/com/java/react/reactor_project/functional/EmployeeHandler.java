package com.java.react.reactor_project.functional;

import com.java.react.reactor_project.documents.Employee;
import com.java.react.reactor_project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class EmployeeHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Mono<ServerResponse> response404 = ServerResponse.notFound().build();
    private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(employeeRepository.findAll(), Employee.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return employeeRepository.findById(id)
                .flatMap(employee -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(employee)))
                .switchIfEmpty(response404);
    }

    public Mono<ServerResponse> getByEmail(ServerRequest serverRequest) {
        String email = serverRequest.pathVariable("email");
        return employeeRepository.findFirstByEmail(email)
                .flatMap(employee -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(employee)))
                .switchIfEmpty(response404);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        Mono<Employee> employeeRequest = serverRequest.bodyToMono(Employee.class);
        return employeeRequest
                .flatMap(employee -> employeeRepository.save(employee)
                        .flatMap(saved -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(saved))))
                .switchIfEmpty(response406);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Mono<Employee> employeeRequest = serverRequest.bodyToMono(Employee.class);
        String employeeId = serverRequest.pathVariable("id");

        Mono<Employee> updatedEmployee = employeeRequest
                .flatMap(request -> employeeRepository.findById(employeeId)
                        .flatMap(employeeDB -> {
                            employeeDB.setName(request.getName());
                            employeeDB.setPhone(request.getPhone());
                            employeeDB.setEmail(request.getEmail());
                            return employeeRepository.save(employeeDB);
                        }));

        return updatedEmployee.flatMap(savedEmployee ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(savedEmployee)))
                .switchIfEmpty(response406);
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String employeeId = serverRequest.pathVariable("id");
        Mono<Void> deletedEmployee = employeeRepository.deleteById(employeeId);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deletedEmployee, Void.class);
    }
}
