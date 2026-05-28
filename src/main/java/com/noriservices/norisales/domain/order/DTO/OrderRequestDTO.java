package com.noriservices.norisales.domain.order.DTO;

import com.noriservices.norisales.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(OrderStatus status,
                              BigDecimal totalPrice,
                              List<OrderItemDTO> items,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {
}
