package com.noriservices.norisales.product.dto;

import com.noriservices.norisales.category.dto.CategoryResponseDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequestDTO(UUID id,
                                String name,
                                CategoryResponseDTO category,
                                String description,
                                boolean active,
                                BigDecimal price,
                                long availableQuantity) {
}
