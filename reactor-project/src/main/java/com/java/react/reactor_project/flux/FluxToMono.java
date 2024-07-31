package com.java.react.reactor_project.flux;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

public class FluxToMono {
    public static void main(String[] args) throws IOException {
        Flux<Integer> number = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .delayElements(Duration.ofSeconds(1));

        //The count will be emitted when onComplete is observed.
        Mono<Long> count = number.count();
        count.subscribe(System.out::println);

        System.out.println("Press a key to finish");
        System.in.read();
    }
}
