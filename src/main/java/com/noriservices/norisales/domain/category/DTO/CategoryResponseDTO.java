package com.noriservices.norisales.domain.category.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO(UUID id,
                                  String name,
                                  String description,
                                  boolean active,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
}
