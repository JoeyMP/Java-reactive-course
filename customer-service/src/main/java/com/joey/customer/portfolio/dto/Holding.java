package com.joey.customer.portfolio.dto;

import com.joey.customer.portfolio.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {
}
