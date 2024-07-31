package com.java.react.reactor_project.controller;

import com.java.react.reactor_project.documents.Employee;
import com.java.react.reactor_project.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employee")
    public Flux<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @GetMapping("employee/{id}")
    public Mono<ResponseEntity<Employee>> findById(@PathVariable String id) {
        return employeeRepository.findById(id)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/employee/email/{email}")
    public Mono<ResponseEntity<Employee>> findByEmail(@PathVariable String email) {
        return employeeRepository.findFirstByEmail(email)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/employee")
    public Mono<ResponseEntity<Employee>> save(@RequestBody Employee employee) {
        return employeeRepository.insert(employee)
                .map(saved -> new ResponseEntity<>(saved, HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(employee, HttpStatus.NOT_ACCEPTABLE));
    }

    @PutMapping("/employee/{id}")
    public Mono<ResponseEntity<Employee>> save(@RequestBody Employee employee, @PathVariable String id) {
        return employeeRepository.findById(id)
                .flatMap(e -> {
                    employee.setId(id);
                    return employeeRepository.save(employee)
                            .map(saved -> new ResponseEntity<>(saved, HttpStatus.ACCEPTED));
                })
                .defaultIfEmpty(new ResponseEntity<>(employee, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/employee/{id}")
    public Mono<Void> delete(@PathVariable String id){
        return employeeRepository.deleteById(id);
    }
}
