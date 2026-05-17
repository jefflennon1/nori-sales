package com.noriservices.norisales.domain.user.dto;

import com.noriservices.norisales.domain.user.UserRole;

public record RegisterUserDTO(String username, String name, String email, String password, UserRole role) {
}
