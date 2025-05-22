package com.ozono.ia.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDto (
        String firstName,
        String lastName,
        LocalDate birthDate,
        String email,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
