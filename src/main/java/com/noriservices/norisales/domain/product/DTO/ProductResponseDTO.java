package com.noriservices.norisales.domain.product.DTO;

import com.noriservices.norisales.domain.category.DTO.CategoryResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDTO(UUID id, String name, CategoryResponseDTO category, String description, BigDecimal price, long availableQuantity, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
