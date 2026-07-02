package com.noriservices.norisales.order.DTO;

import java.util.UUID;

public record OrderItemRequestDTO(UUID productId, int quantity) {
}
