package com.java.react.reactor_project.flux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class FluxToMono2 {

    public static void main(String[] args) throws IOException {
        Flux<Integer> number = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .delayElements(Duration.ofSeconds(1));

        //Stream is series of items all available at once while the flux is items that can come in asynchronously over time
//        List<Integer> listNumber = number.toStream().collect(Collectors.toList());
//        System.out.println("List is " + listNumber);
//        System.out.println("Size: " + listNumber.size());

        //Using reactive programming
        Mono<List<Integer>> numbersMono = number
                .collectList();
        numbersMono.subscribe(System.out::println);


        System.out.println("Press a key to finish");
        System.in.read();
    }
}
