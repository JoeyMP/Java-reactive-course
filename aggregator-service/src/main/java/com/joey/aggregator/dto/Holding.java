package com.joey.aggregator.dto;


import com.joey.aggregator.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {
}
