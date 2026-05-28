package com.noriservices.norisales.domain.order.DTO;

import com.noriservices.norisales.domain.product.DTO.ProductResponseDTO;

import java.math.BigDecimal;

public record OrderItemDTO(ProductResponseDTO product,
                           int quantity,
                           BigDecimal unitPrice,
                           BigDecimal subtotal) {
}
