package com.java.react.reactor_project;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static reactor.core.scheduler.Schedulers.parallel;

public class FlatMapOperator {

    @Test
    public void flatMap() {
        List<String> employeeIds = List.of("1", "2", "3", "4", "5");
        Flux<String> employeeName = Flux.fromIterable(employeeIds)//Flux<String>
                .flatMap(id -> getEmployeeDetail(id))//DB or external call service that return a flux or mono // Mono<String>
                .log();

        StepVerifier.create(employeeName)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void flatMap_ParallelSchedule() {
        List<String> employeeIds = List.of("1", "2", "3", "4", "5", "6");
        Flux<String> employeeName = Flux.fromIterable(employeeIds)//Flux<String>
                .window(2)// Flux<Flux<String>>
                .flatMapSequential(identifiers -> identifiers.flatMap(
                                id -> getEmployeeDetail(id)
                        ).subscribeOn(parallel())
                )
                .log();

        StepVerifier.create(employeeName)
                .expectNextCount(6)
                .verifyComplete();
    }

    private Mono<String> getEmployeeDetail(String id) {
        Map<String, String> employee = new HashMap<>();
        employee.put("1", "A");
        employee.put("2", "B");
        employee.put("3", "C");
        employee.put("4", "D");
        employee.put("5", "E");
        employee.put("6", "F");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(employee.getOrDefault(id, "not found"));
    }
}
