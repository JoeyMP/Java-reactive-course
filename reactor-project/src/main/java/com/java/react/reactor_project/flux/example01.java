package com.java.react.reactor_project.flux;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

public class example01 {

    public static void main(String[] args) throws IOException {
        Flux<String> flux = Flux.fromArray(new String[]{"data 1", "data 2", "data 3"})
                .delayElements(Duration.ofSeconds(1));
        //flux.subscribe(System.out::println);

        flux.doOnNext(System.out::println).subscribe();

        Flux<String> flux2 = Flux.fromArray(new String[]{"data 4", "data 5", "data 6"})
                .delayElements(Duration.ofSeconds(1));
        //flux.subscribe(System.out::println);

        flux2.doOnNext(System.out::println).subscribe();

        System.out.println("Press a key to finish");
        System.in.read();
    }

}
