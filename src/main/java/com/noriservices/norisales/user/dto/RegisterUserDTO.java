package com.noriservices.norisales.user.dto;

import com.noriservices.norisales.user.UserRole;

public record RegisterUserDTO(String username, String name, String email, String password, UserRole role) {
}
