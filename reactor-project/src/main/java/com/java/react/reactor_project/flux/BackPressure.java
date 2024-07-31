package com.java.react.reactor_project.flux;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

public class BackPressure {
    public static void main(String[] args) throws IOException {

        Flux<Integer> number = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .delayElements(Duration.ofSeconds(1));


        /*number.subscribe(
                data -> System.out.println(data),//onNext
                error -> System.out.println(error),//onError, terminal process
                () -> System.out.println("Complete")//onComplete, terminal process
        );*/

        //same behavior using backpressure
        number.subscribe(new MySubscriber<>());


        System.out.println("Press a key to finish");
        System.in.read();
    }

    static class MySubscriber<T> extends BaseSubscriber<T> {

        public void hookOnSubscribe(Subscription subscription) {
            System.out.println("Subscribe happened");
            request(1);
        }

        public void hookOnNext(T value) {
            System.out.println(value.toString() + " received");
            request(1);
        }
    }
}
