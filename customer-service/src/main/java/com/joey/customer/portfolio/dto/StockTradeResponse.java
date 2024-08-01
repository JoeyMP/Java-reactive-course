package com.joey.customer.portfolio.dto;

import com.joey.customer.portfolio.domain.Ticker;
import com.joey.customer.portfolio.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
                                 Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction action,
                                 Integer totalPrice,
                                 Integer balance) {
}
