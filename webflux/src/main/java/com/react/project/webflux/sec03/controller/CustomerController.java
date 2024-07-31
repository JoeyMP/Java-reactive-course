package com.react.project.webflux.sec03.controller;

import com.react.project.webflux.sec03.dto.CustomerDto;
import com.react.project.webflux.sec03.service.CustomerService;
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
    private Mono<ResponseEntity<CustomerDto>> getById(@PathVariable Integer id) {
        return customerService.getById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    private Mono<CustomerDto> save(@RequestBody Mono<CustomerDto> customer) {
        return customerService.saveCustomer(customer);
    }

    @PutMapping("/{id}")
    private Mono<ResponseEntity<CustomerDto>> update(@PathVariable Integer id, @RequestBody Mono<CustomerDto> customer) {
        return customerService.updateCustomer(id, customer)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    private Mono<Void> deleteById(@PathVariable Integer id) {
        return customerService.delete(id);
    }

    @DeleteMapping("/v2/{id}")
    private Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable Integer id) {
        return customerService.deleteCustomerById(id)
                .filter(b -> b)
                .map(b -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
