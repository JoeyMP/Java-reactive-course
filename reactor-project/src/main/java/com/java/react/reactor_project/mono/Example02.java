package com.java.react.reactor_project.mono;

import reactor.core.publisher.Mono;

public class Example02 {

    public static void main(String[] args) {
        Mono<String> mono = Mono.fromSupplier(() -> {
            throw new RuntimeException("Throwing exception");
        });
        mono.subscribe(
                data -> System.out.println(data),
                error -> System.out.println(error),
                () -> System.out.println("Complete")
        );
    }
}
