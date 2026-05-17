package com.noriservices.norisales.domain.user.dto;

import com.noriservices.norisales.domain.user.UserRole;

public record ResponseUserDTO(String username, String email, UserRole role, boolean enabled) {
}
