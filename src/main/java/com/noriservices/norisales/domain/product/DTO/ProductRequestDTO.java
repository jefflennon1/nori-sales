package com.noriservices.norisales.domain.product.DTO;

import com.noriservices.norisales.domain.category.DTO.CategoryResponseDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequestDTO(UUID id, String name, CategoryResponseDTO category, String description, boolean active, BigDecimal price, long availableQuantity) {
}
