package com.noriservices.norisales.order.event.DTO;

import java.util.UUID;

public record OrderItemEventDTO(
        UUID productId,
        int quantity
) {}

