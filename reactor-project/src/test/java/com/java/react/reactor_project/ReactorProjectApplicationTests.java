package com.java.react.reactor_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@SpringBootTest
class ReactorProjectApplicationTests {

    @Autowired
    private Service01 service01;

    @Test
    void testFindOne() {
        Mono<String> one = service01.findOne();
        StepVerifier.create(one).expectNext("Hi").verifyComplete();
    }

    @Test
    void testFindAll() {
        Flux<String> all = service01.findAll();
        StepVerifier.create(all)
                .expectNext("Hi")
                .expectNext("how")
                .expectNext("are")
                .expectNext("you")
                .verifyComplete();
    }

    @Test
    void testFindAllLazy() {
        Flux<String> all = service01.findAllLazy();
        StepVerifier.create(all)
                .expectNext("Hi")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("how")

                .thenAwait(Duration.ofSeconds(1))
                .expectNext("are")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("you")
                .thenAwait(Duration.ofSeconds(1))
                .verifyComplete();
    }

    @Test
    void transform() {
        AtomicInteger atomicInteger = new AtomicInteger();
        Function<Flux<String>, Flux<String>> function = f -> {
            if (atomicInteger.incrementAndGet() == 1) {
                return f.filter(color -> !color.contains("orange"))
                        .map(String::toUpperCase);
            } else {
                return f.filter(color -> !color.contains("purple"))
                        .map(String::toUpperCase);
            }
        };

        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        .transform(function);
		//every subscribe start atomicInteger
        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }

    @Test
    void transformDeferred() {
        AtomicInteger atomicInteger = new AtomicInteger();

        Function<Flux<String>, Flux<String>> function = f -> {
            if (atomicInteger.incrementAndGet() == 1) {
                return f.filter(color -> !color.contains("orange"))
                        .map(String::toUpperCase);
            } else {
                return f.filter(color -> !color.contains("purple"))
                        .map(String::toUpperCase);
            }
        };
        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        .transformDeferred(function);

        //all subscribe share atomicInteger value
        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }
}
