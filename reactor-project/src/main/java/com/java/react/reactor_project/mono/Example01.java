package com.java.react.reactor_project.mono;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

public class Example01 {

    public static void main(String[] args) throws IOException {
        Mono<String> mono = Mono.just("Test");
        mono.subscribe(
                data -> System.out.println(data),//onNext
                error -> System.out.println(error),//onError
                () -> System.out.println("Complete")//onComplete
        );
    }
}
