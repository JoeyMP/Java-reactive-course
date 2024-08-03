package com.joey.aggregator.dto;


import com.joey.aggregator.domain.Ticker;
import com.joey.aggregator.domain.TradeAction;

public record StockTradeResponse(Integer customerId,
                                 Ticker ticker,
                                 Integer price,
                                 Integer quantity,
                                 TradeAction action,
                                 Integer totalPrice,
                                 Integer balance) {
}
