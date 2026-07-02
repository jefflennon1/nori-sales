package com.noriservices.norisales.order.dto;

import com.noriservices.norisales.product.dto.ProductResponseDTO;

import java.math.BigDecimal;

public record OrderItemDTO(ProductResponseDTO product,
                           int quantity,
                           BigDecimal unitPrice,
                           BigDecimal subtotal) {
}
