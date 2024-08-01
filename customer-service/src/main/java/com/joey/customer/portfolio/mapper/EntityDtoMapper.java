package com.joey.customer.portfolio.mapper;

import com.joey.customer.portfolio.domain.Ticker;
import com.joey.customer.portfolio.dto.CustomerInformation;
import com.joey.customer.portfolio.dto.Holding;
import com.joey.customer.portfolio.dto.StockTradeRequest;
import com.joey.customer.portfolio.dto.StockTradeResponse;
import com.joey.customer.portfolio.entity.Customer;
import com.joey.customer.portfolio.entity.PortfolioItem;

import java.util.List;

public class EntityDtoMapper {

    public static CustomerInformation toCustomerInformation(Customer customer, List<PortfolioItem> item) {
        var holding = item.stream()
                .map(i -> new Holding(i.getTicker(), i.getQuantity()))
                .toList();
        return new CustomerInformation(customer.getId(), customer.getName(), customer.getBalance(), holding);
    }

    public static PortfolioItem toPortfolioItem(Integer customerId, Ticker ticker) {
        PortfolioItem item = new PortfolioItem();
        item.setCustomerId(customerId);
        item.setTicker(ticker);
        item.setQuantity(0);
        return item;
    }

    public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, Integer customerId, Integer balance) {
        return new StockTradeResponse(customerId, request.ticker(), request.price(), request.quantity(), request.action(), request.totalPrice(), balance);
    }
}
