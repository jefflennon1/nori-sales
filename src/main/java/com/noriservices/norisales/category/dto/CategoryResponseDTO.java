package com.noriservices.norisales.category.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO(UUID id,
                                  String name,
                                  String description,
                                  boolean active,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
}
