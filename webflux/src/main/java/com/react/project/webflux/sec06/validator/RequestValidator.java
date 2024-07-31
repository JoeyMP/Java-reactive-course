package com.react.project.webflux.sec06.validator;

import com.react.project.webflux.sec06.dto.CustomerDto;
import com.react.project.webflux.sec06.exception.ApplicationException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDto>> validate() {
        return mono -> mono.filter(hasName())
                .switchIfEmpty(ApplicationException.missingName())
                .filter(hasEmail())
                .switchIfEmpty(ApplicationException.missingValidEmail());
    }

    private static Predicate<CustomerDto> hasName() {
        return dto -> Objects.nonNull(dto.name());
    }

    private static Predicate<CustomerDto> hasEmail() {
        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@");
    }
}
