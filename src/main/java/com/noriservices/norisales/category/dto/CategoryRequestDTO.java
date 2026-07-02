package com.noriservices.norisales.category.dto;

import java.util.UUID;

public record CategoryRequestDTO(UUID id, String name, String description, boolean active) {
}
