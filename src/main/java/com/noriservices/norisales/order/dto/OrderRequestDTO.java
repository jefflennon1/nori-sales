package com.noriservices.norisales.order.dto;

import java.util.List;

public record OrderRequestDTO(List<OrderItemRequestDTO> items) {
}
