package com.noriservices.norisales.order.dto;

import java.util.UUID;

public record OrderItemRequestDTO(UUID productId, int quantity) {
}
