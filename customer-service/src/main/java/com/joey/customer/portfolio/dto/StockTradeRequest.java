package com.joey.customer.portfolio.dto;

import com.joey.customer.portfolio.domain.Ticker;
import com.joey.customer.portfolio.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
                                Integer price,
                                Integer quantity,
                                TradeAction action) {

    public Integer totalPrice() {
        return price * quantity;
    }
}
