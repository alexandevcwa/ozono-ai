package com.ozono.ia.mapper;

import com.ozono.ia.dto.FileDto;
import com.ozono.ia.model.File;

public class FileMapper {

    public static FileDto convertToDto(File entity) {
        return new FileDto(
                entity.getFileName(),
                entity.getFileType(),
                entity.getFileSize(),
                entity.getFileUrl(),
                entity.getFileUuid(),
                entity.getFileExtension()
        );
    }

}
