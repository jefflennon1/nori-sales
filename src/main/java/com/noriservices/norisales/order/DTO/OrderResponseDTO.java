package com.noriservices.norisales.order.DTO;

import com.noriservices.norisales.order.OrderStatus;
import com.noriservices.norisales.user.dto.ResponseUserDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record   OrderResponseDTO(UUID id,
                               ResponseUserDTO user,
                               OrderStatus status,
                               BigDecimal totalPrice,
                               List<OrderItemDTO> items,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt) {
}
