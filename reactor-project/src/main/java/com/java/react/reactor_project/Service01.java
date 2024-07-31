package com.java.react.reactor_project;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class Service01 {

    public Mono<String> findOne() {
        return Mono.just("Hi");
    }

    public Flux<String> findAll() {
        return Flux.just("Hi", "how", "are", "you");
    }

    public Flux<String> findAllLazy() {
        return Flux.just("Hi", "how", "are", "you").delaySequence(Duration.ofSeconds(10));
    }
}
