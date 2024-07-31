package com.java.react.reactor_project.flux;

import reactor.core.publisher.Flux;

public class example02 {

    public static void main(String[] args) {
        Flux<String> names = Flux.just("Cristhian Ramirez", "Joey Melendez", "Daniel Navarro");
        Flux<Person> fluxUser = names
                .map(name -> new Person(name.split(" ")[0].toUpperCase(), name.split(" ")[1].toUpperCase()))
                .filter(user -> user.getLastName().equalsIgnoreCase("Melendez"))
                .doOnNext(user -> {
                    if (user == null) throw new RuntimeException("empty name");
                    System.out.println(user.getFirstName().concat(" ").concat(user.getLastName()));
                })
                .map(user -> {
                    user.setFirstName(user.getFirstName().toLowerCase());
                    return user;
                });

        fluxUser.subscribe(user -> System.out.println(user.toString()),
                error -> System.out.println(error.getMessage()),
                () -> System.out.println("Complete"));
    }
}
