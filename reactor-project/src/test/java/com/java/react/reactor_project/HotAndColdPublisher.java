package com.java.react.reactor_project;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.stream.Stream;

public class HotAndColdPublisher {

    @Test
    public void coldTest() throws InterruptedException {
        Flux<String> flux = Flux.fromStream(HotAndColdPublisher::getVideo)
                .delayElements(Duration.ofSeconds(2));

        flux.subscribe(part -> System.out.println("Subscriber 1: " + part));
        Thread.sleep(5000);
        flux.subscribe(part -> System.out.println("Subscriber 2: " + part));
        Thread.sleep(5000);
    }

    @Test
    public void hotTest() throws InterruptedException {
        Flux<String> flux = Flux.fromStream(HotAndColdPublisher::getVideo)
                .delayElements(Duration.ofSeconds(2))
                .share();

        flux.subscribe(part -> System.out.println("Subscriber 1: " + part));
        Thread.sleep(5000);
        flux.subscribe(part -> System.out.println("Subscriber 2: " + part));
        Thread.sleep(5000);
    }

    private static Stream<String> getVideo() {
        System.out.println("Request video stream");
        return Stream.of("part 1", "part 2", "part 3", "part 4", "part 5");
    }
}
