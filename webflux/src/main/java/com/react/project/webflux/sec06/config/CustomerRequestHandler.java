package com.react.project.webflux.sec06.config;

import com.react.project.webflux.sec06.dto.CustomerDto;
import com.react.project.webflux.sec06.exception.ApplicationException;
import com.react.project.webflux.sec06.service.CustomerService;
import com.react.project.webflux.sec06.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class CustomerRequestHandler {

    @Autowired
    private CustomerService customerService;

    public Mono<ServerResponse> allCustomer(ServerRequest request) {
        return this.customerService.getAllCustomer()
                .as(flux -> ServerResponse.ok().body(flux, CustomerDto.class));
    }

    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return this.customerService.getById(id)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> paginatedCustomer(ServerRequest request) {
        var page = request.queryParam("page").map(Integer::parseInt).orElse(1);
        var size = request.queryParam("size").map(Integer::parseInt).orElse(3);
        return this.customerService.getAllCustomer(page, size)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(this.customerService::saveCustomer)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(validateReq -> this.customerService.updateCustomer(id, validateReq))
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return this.customerService.deleteCustomerById(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .then(ServerResponse.ok().build());
    }
}
