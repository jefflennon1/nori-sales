package com.noriservices.norisales.order.DTO;

import java.util.List;

public record OrderRequestDTO(List<OrderItemRequestDTO> items) {
}
