package com.joey.aggregator.dto;

import com.joey.aggregator.domain.Ticker;

import java.time.LocalDateTime;

public record PriceUpdate(Ticker ticker,
                          Integer price,
                          LocalDateTime time) {
}
