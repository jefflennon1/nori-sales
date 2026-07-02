package com.noriservices.norisales.user.dto;

import com.noriservices.norisales.user.UserRole;

public record ResponseUserDTO(String username, String email, UserRole role, boolean active) {
}
