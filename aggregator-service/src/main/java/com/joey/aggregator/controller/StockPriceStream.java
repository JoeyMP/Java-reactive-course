package com.joey.aggregator.controller;

import com.joey.aggregator.client.StockServiceClient;
import com.joey.aggregator.dto.PriceUpdate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("stock")
public class StockPriceStream {

    private final StockServiceClient stockServiceClient;

    public StockPriceStream(StockServiceClient stockServiceClient) {
        this.stockServiceClient = stockServiceClient;
    }

    @GetMapping(value = "/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PriceUpdate> priceUpdateStream() {
        return this.stockServiceClient.priceUpdatedStream();
    }
}
