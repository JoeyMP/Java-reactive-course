package com.react.project.webflux.sec04.controller;

import com.react.project.webflux.sec04.dto.CustomerDto;
import com.react.project.webflux.sec04.exception.ApplicationException;
import com.react.project.webflux.sec04.service.CustomerService;
import com.react.project.webflux.sec04.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    private Flux<CustomerDto> getAll() {
        return customerService.getAllCustomer();
    }

    @GetMapping("paginated")
    private Mono<List<CustomerDto>> getAll(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "3") Integer size) {
        return customerService.getAllCustomer(page, size)
                .collectList();
    }

    @GetMapping("/{id}")
    private Mono<CustomerDto> getById(@PathVariable Integer id) {
        return customerService.getById(id)
                .switchIfEmpty(ApplicationException.customerNotFound(id));
    }

    @PostMapping
    private Mono<CustomerDto> save(@RequestBody Mono<CustomerDto> customer) {
        return customer.transform(RequestValidator.validate())
                .as(this.customerService::saveCustomer);
    }

    @PutMapping("/{id}")
    private Mono<CustomerDto> update(@PathVariable Integer id, @RequestBody Mono<CustomerDto> customer) {
        return customer.transform(RequestValidator.validate())
                .as(validRequest -> this.customerService.updateCustomer(id, validRequest))
                .switchIfEmpty(ApplicationException.customerNotFound(id));
    }

    @DeleteMapping("/{id}")
    private Mono<Void> deleteById(@PathVariable Integer id) {
        return customerService.delete(id);
    }

    @DeleteMapping("/v2/{id}")
    private Mono<Void> deleteCustomerById(@PathVariable Integer id) {
        return customerService.deleteCustomerById(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .then();
    }
}
