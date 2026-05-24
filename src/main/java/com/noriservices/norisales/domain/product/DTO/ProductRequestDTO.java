package com.noriservices.norisales.domain.product.DTO;

import com.noriservices.norisales.domain.category.DTO.CategoryResponseDTO;

import java.math.BigDecimal;

public record ProductRequestDTO(String name, CategoryResponseDTO category, String description, BigDecimal price, long availableQuantity) {
}
