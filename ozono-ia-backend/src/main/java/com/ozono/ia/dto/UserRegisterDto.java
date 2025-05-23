package com.ozono.ia.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRegisterDto(
        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        @Size(max = 50, message = "Email must be less than 50 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 25, message = "Password must be between 8 and 25 characters")
        String password,

        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must be less than 75 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must be less than 75 characters")
        String lastName,

        @NotNull(message = "Birthdate is required")
        @Past(message = "Birthdate must be in the past")
        LocalDate birthDate
) {
}
