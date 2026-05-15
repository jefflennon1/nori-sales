package com.noriservices.norisales.domain.user.dto;

import com.noriservices.norisales.domain.user.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
