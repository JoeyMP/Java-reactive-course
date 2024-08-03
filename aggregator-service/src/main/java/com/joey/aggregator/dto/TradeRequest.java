package com.joey.aggregator.dto;

import com.joey.aggregator.domain.Ticker;
import com.joey.aggregator.domain.TradeAction;

public record TradeRequest(Ticker ticker,
                           TradeAction action,
                           Integer quantity) {
}
