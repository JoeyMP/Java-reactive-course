package com.react.project.webflux.sec02.dto;

import java.time.Instant;
import java.util.UUID;

public record OrderDetail(UUID orderId,
                          String customerName,
                          String productName,
                          Integer amount,
                          Instant orderDate) {
}
