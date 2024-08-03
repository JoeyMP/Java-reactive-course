package com.joey.aggregator.dto;

import com.joey.aggregator.domain.Ticker;

public record StockPriceResponse(Ticker ticker,
                                 Integer price) {
}
