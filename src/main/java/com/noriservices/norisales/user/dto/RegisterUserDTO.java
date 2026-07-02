package com.noriservices.norisales.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(@NotBlank
                              @Size(min = 3, max = 30)
                              String username,

                              @NotBlank
                              @Size(max = 150)
                              String name,

                              @NotBlank
                              @Email
                              @Size(max = 100)
                              String email,

                              @NotBlank
                              @Size(min = 8, max = 100)
                              String password) {
}
