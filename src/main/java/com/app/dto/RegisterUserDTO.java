package com.app.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterUserDTO(@NotBlank String username,
                              @NotBlank String email,
                              @NotBlank String password) {
}
