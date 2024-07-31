package com.java.react.reactor_project;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class CombineReactiveStream {

    @Test
    public void testMergeFlux() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");
        Flux<String> fluxMerge = Flux.merge(flux1, flux2).log();

        StepVerifier.create(fluxMerge)
                .expectNext("A", "B", "C", "D", "E", "F")
                .expectComplete()
                .verify();
    }

    @Test
    public void testMergeFluxWithDelay() {
        //this does not keep order, as alternative you can use concat operator
        Flux<String> fluxA = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxB = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxConcat = Flux.merge(fluxA, fluxB).log();

        StepVerifier.create(fluxConcat)
                .expectNextCount(6)
                .expectComplete()
                .verify();
    }

    @Test
    public void testConcatFlux() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");
        Flux<String> fluxConcat = Flux.concat(flux1, flux2).log();

        StepVerifier.create(fluxConcat)
                .expectNext("A", "B", "C", "D", "E", "F")
                .expectComplete()
                .verify();
    }

    @Test
    public void testConcatFluxWithDelay() {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxConcat = Flux.concat(flux1, flux2).log();

        StepVerifier.create(fluxConcat)
                .expectNext("A", "B", "C", "D", "E", "F")
                .expectComplete()
                .verify();
    }

    @Test
    public void testConcatFlux_With() {
        Flux<Integer> fluxA = Flux.just(1, 2, 3, 4, 5);
        Mono<Integer> monoA = Mono.just(6);
        Flux<Integer> fluxConcat = fluxA.concatWith(monoA).log();

        StepVerifier.create(fluxConcat)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .expectNext(6)
                .expectComplete()
                .verify();
    }

    @Test
    public void testZipFlux() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");
        Flux<Tuple2<String, String>> fluxZip = Flux.zip(flux1, flux2); //[A,D],[B,E],[C,F]
        Flux<String> finalflux = fluxZip.map(t -> t.getT1() + t.getT2()); //"AD","BE","CF"

        StepVerifier.create(finalflux.log())
                .expectNext("AD","BE","CF")
                .expectComplete()
                .verify();
    }

    @Test
    public void testZipFlux_With() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");
        Flux<Tuple2<String, String>> fluxZip = flux1.zipWith(flux2); //[A,D],[B,E],[C,F]
        Flux<String> finalflux = fluxZip.map(t -> t.getT1() + t.getT2()); //"AD","BE","CF"

        StepVerifier.create(finalflux.log())
                .expectNext("AD","BE","CF")
                .expectComplete()
                .verify();
    }

}
