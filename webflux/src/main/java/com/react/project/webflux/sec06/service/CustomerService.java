package com.react.project.webflux.sec06.service;

import com.react.project.webflux.sec06.dto.CustomerDto;
import com.react.project.webflux.sec06.mapper.EntityDtoMapper;
import com.react.project.webflux.sec06.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllCustomer() {
        return customerRepository.findAll()
                .map(EntityDtoMapper::toDto);
    }

    public Flux<CustomerDto> getAllCustomer(Integer page, Integer size) {
        return customerRepository.findBy(PageRequest.of(page - 1, size))//index start with zero
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> getById(Integer id) {
        return customerRepository.findById(id)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> dto) {
        return dto.map(EntityDtoMapper::toEntity)
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> updateCustomer(Integer id, Mono<CustomerDto> dto) {
        return customerRepository.findById(id)
                .flatMap(entity -> dto)
                .map(EntityDtoMapper::toEntity)
                .doOnNext(d -> d.setId(id))
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Void> delete(Integer id) {
        return customerRepository.deleteById(id);
    }

    public Mono<Boolean> deleteCustomerById(Integer id) {
        return customerRepository.deleteCustomerById(id);
    }
}
