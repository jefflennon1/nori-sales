package com.noriservices.norisales.infra.kafka;

import java.util.UUID;

public record OrderItemEventDTO(
        UUID productId,
        int quantity
) {}

