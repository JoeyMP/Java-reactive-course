package com.java.react.reactor_project;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class ErrorsHandlingReactiveStream {

    @Test
    public void errorHandlingTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("some error")))
                .concatWith(Flux.just("D"));

        StepVerifier.create(flux1.log())
                .expectNext("A", "B", "C")
                .expectError()
                .verify();
    }

    @Test
    public void doOnErrorTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("some error")))
                .doOnError(error -> System.out.println("Some error occurred " + error));

        StepVerifier.create(flux1.log())
                .expectNext("A", "B", "C")
                .expectError()
                .verify();
    }

    @Test
    public void onErrorReturnTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("some error")))
                .onErrorReturn("Some default value");

        StepVerifier.create(flux1.log())
                .expectNext("A", "B", "C")
                .expectNext("Some default value")
                .verifyComplete();
    }

    @Test
    public void onErrorResumeTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("some error")))
                .onErrorResume(error -> {
                    System.out.println(error);
                    return Flux.just("D");
                });

        StepVerifier.create(flux1.log())
                .expectNext("A", "B", "C", "D")
                .verifyComplete();
    }

    @Test
    public void onErrorContinueTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C", "D", "E")
                .map(element -> {
                    if (element.equals("B") || element.equals("D")) {
                        throw new RuntimeException("some error");
                    }
                    return element;
                })
                .onErrorContinue((error, element) -> {
                    System.out.println("Exception : " + error);
                    System.out.println("Exception element: " + element);
                });

        StepVerifier.create(flux1.log())
                .expectNext("A", "C", "E")
                .verifyComplete();
    }

    @Test
    public void onErrorMapTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C", "D", "E")
                .map(element -> {
                    if (element.equals("B") || element.equals("D")) {
                        throw new RuntimeException("business error");
                    }
                    return element;
                })
                .onErrorMap(error -> {
                    if (error.getMessage().equals("business error")) {
                        return new CustomException("Translated exception", error);
                    } else {
                        return error;
                    }
                });

        StepVerifier.create(flux1.log())
                .expectNext("A")
                .expectErrorMessage("Translated exception")
                .verify();
    }

    @Test
    public void doFinallyTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C", "D", "E")
                .delayElements(Duration.ofSeconds(1))
                .doFinally(signalType -> {
                    if (signalType.equals(SignalType.CANCEL))
                        System.out.println("Perform operation on cancel");
                    if (signalType.equals(SignalType.ON_COMPLETE))
                        System.out.println("Perform operation on complete");
                    if (signalType.equals(SignalType.ON_ERROR))
                        System.out.println("Perform operation on error");
                })
                .take(2);

        StepVerifier.create(flux1.log())
                .expectNext("A", "B")
                .thenCancel()
                .verify();
    }

    @Test
    public void onErrorCompleteTest() {
        Flux<String> flux1 = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("some error")))
                .onErrorComplete();

        StepVerifier.create(flux1.log())
                .expectNext("A", "B", "C")
                .verifyComplete();
    }


    class CustomException extends Exception {
        public CustomException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
