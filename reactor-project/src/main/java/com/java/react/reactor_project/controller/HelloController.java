package com.java.react.reactor_project.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/mono")
    public Mono<String> getMono() {
        return Mono.just("Welcome guys").log();
    }

    @GetMapping("/flux")
    public Flux<Integer> getFlux() {
        return Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .delayElements(Duration.ofSeconds(2)).log();
    }

    @GetMapping(path = "/fluxStream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getFluxStream() {
        return Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .delayElements(Duration.ofSeconds(2)).log();
    }

    @GetMapping("/demo")
    public Mono<String> demo() {
        return computeMessage().zipWith(getNameFromDB())
                .map(value -> value.getT1() + value.getT2());
    }

    private Mono<String> computeMessage() {
        return Mono.just("Hello ").delayElement(Duration.ofSeconds(6));
    }

    private Mono<String> getNameFromDB() {
        return Mono.just("Joey ").delayElement(Duration.ofSeconds(8));
    }
}
