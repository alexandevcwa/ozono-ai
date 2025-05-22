package com.ozono.ia.dto;

public record FileDto(
        String fileName,
        String fileType,
        Long fileSize,
        String fileUrl,
        String fileUuid,
        String fileExtension
) {
}
