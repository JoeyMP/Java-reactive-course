package com.joey.aggregator.service;

import com.joey.aggregator.client.CustomerServiceClient;
import com.joey.aggregator.client.StockServiceClient;
import com.joey.aggregator.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerPortfolioService {

    private final CustomerServiceClient customerServiceClient;
    private final StockServiceClient stockServiceClient;

    public CustomerPortfolioService(CustomerServiceClient customerServiceClient, StockServiceClient stockServiceClient) {
        this.customerServiceClient = customerServiceClient;
        this.stockServiceClient = stockServiceClient;
    }

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return customerServiceClient.getCustomerInformation(customerId);
    }

    public Mono<StockTradeResponse> getStock(Integer customerId, StockTradeRequest request) {
        return customerServiceClient.trade(customerId, request);
    }

    public Mono<StockTradeResponse> trade(Integer customerId, TradeRequest request) {
        return this.stockServiceClient.getStockPrice(request.ticker())
                .map(StockPriceResponse::price)
                .map(p -> toStockTradeRequest(request, p))
                .flatMap(req -> customerServiceClient.trade(customerId, req));
    }

    private StockTradeRequest toStockTradeRequest(TradeRequest request, Integer price) {
        return new StockTradeRequest(request.ticker(), price, request.quantity(), request.action());
    }
}
