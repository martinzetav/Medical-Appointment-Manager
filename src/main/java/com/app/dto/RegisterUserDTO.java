package com.app.dto;

import com.app.model.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserDTO(@NotBlank String username,
                              @NotBlank String email,
                              @NotBlank String password,
                              @NotNull RoleEnum role) {
}
