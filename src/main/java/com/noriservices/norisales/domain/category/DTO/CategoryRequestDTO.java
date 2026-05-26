package com.noriservices.norisales.domain.category.DTO;

import java.util.UUID;

public record CategoryRequestDTO(UUID id, String name, String description, boolean active) {
}
