package com.noriservices.norisales.domain.order.DTO;

import java.util.UUID;

public record OrderItemRequestDTO(UUID productId, int quantity) {
}
