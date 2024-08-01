package com.joey.customer.portfolio.controller;

import com.joey.customer.portfolio.dto.CustomerInformation;
import com.joey.customer.portfolio.dto.StockTradeRequest;
import com.joey.customer.portfolio.dto.StockTradeResponse;
import com.joey.customer.portfolio.service.CustomerService;
import com.joey.customer.portfolio.service.TradeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerController {

    private final CustomerService customerService;
    private final TradeService tradeService;

    public CustomerController(CustomerService customerService, TradeService tradeService) {
        this.customerService = customerService;
        this.tradeService = tradeService;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerInformation> getCustomerInformation(@PathVariable Integer customerId) {
        return this.customerService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customerId}/trade")
    public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<StockTradeRequest> request) {
        return request.flatMap(req -> this.tradeService.trade(customerId, req));

    }
}
