package com.noriservices.norisales.infra.kafka;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderConfirmedEvent(
        UUID orderId,
        UUID buyerId,
        List<OrderItemEventDTO> items,
        LocalDateTime confirmedAt
) {}

