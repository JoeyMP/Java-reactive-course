package com.joey.customer.portfolio.service;

import com.joey.customer.portfolio.dto.StockTradeRequest;
import com.joey.customer.portfolio.dto.StockTradeResponse;
import com.joey.customer.portfolio.entity.Customer;
import com.joey.customer.portfolio.entity.PortfolioItem;
import com.joey.customer.portfolio.exceptions.ApplicationException;
import com.joey.customer.portfolio.mapper.EntityDtoMapper;
import com.joey.customer.portfolio.repository.CustomerRepository;
import com.joey.customer.portfolio.repository.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class TradeService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public TradeService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    @Transactional
    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request) {
        return switch (request.action()) {
            case BUY -> this.buyStock(customerId, request);
            case SELL -> this.sellStock(customerId, request);
        };
    }

    private Mono<StockTradeResponse> buyStock(Integer customerId, StockTradeRequest request) {

        var customerMono = customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationException.customerNotFound(customerId))
                .filter(c -> c.getBalance() >= request.totalPrice())
                .switchIfEmpty(ApplicationException.insufficientBalance(customerId));

        var portfolioItem = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, request.ticker())
                .defaultIfEmpty(EntityDtoMapper.toPortfolioItem(customerId, request.ticker()));

        return customerMono.zipWhen(customer -> portfolioItem)
                .flatMap(t -> this.executeBuy(t.getT1(), t.getT2(), request));
    }

    private Mono<StockTradeResponse> executeBuy(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
        customer.setBalance(customer.getBalance() - request.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() + request.quantity());
        return this.save(customer, portfolioItem, request);
    }

    private Mono<StockTradeResponse> sellStock(Integer customerId, StockTradeRequest request) {
        var customerMono = customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationException.customerNotFound(customerId));

        var portfolioItem = this.portfolioItemRepository.findByCustomerIdAndTicker(customerId, request.ticker())
                .filter(pi -> pi.getQuantity() >= request.quantity())
                .switchIfEmpty(ApplicationException.insufficientShare(customerId));

        return customerMono.zipWhen(customer -> portfolioItem)
                .flatMap(t -> this.executeSell(t.getT1(), t.getT2(), request));
    }

    private Mono<StockTradeResponse> executeSell(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
        customer.setBalance(customer.getBalance() + request.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() - request.quantity());
        return this.save(customer, portfolioItem, request);
    }

    private Mono<StockTradeResponse> save(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request) {
        var response = EntityDtoMapper.toStockTradeResponse(request, customer.getId(), customer.getBalance());
        return Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItem))
                .thenReturn(response);
    }

}
