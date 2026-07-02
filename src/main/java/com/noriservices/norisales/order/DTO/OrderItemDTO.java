package com.noriservices.norisales.order.DTO;

import com.noriservices.norisales.product.DTO.ProductResponseDTO;

import java.math.BigDecimal;

public record OrderItemDTO(ProductResponseDTO product,
                           int quantity,
                           BigDecimal unitPrice,
                           BigDecimal subtotal) {
}
