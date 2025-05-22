package com.ozono.ia.dto;

import jakarta.validation.constraints.NotBlank;

public record UserAuthDto(

        @NotBlank(message = "Username is required")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {
}
