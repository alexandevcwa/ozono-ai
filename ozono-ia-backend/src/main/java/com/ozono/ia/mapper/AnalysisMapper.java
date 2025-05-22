package com.ozono.ia.mapper;

import com.ozono.ia.dto.AnalysisDto;
import com.ozono.ia.model.Analysis;

public class AnalysisMapper {

    public static Analysis convertToEntity(AnalysisDto dto) {
        return Analysis.builder()
                .material(dto.materialType())
                .description(dto.materialDescription())
                .difficulty(dto.difficultyOfRecycle())
                .disintegration(dto.disintegrationTime())
                .contaminationLevel(dto.contaminationLevel())
                .build();
    }

    public static AnalysisDto convertToDto(Analysis entity) {
        return new AnalysisDto(
                entity.getId(),
                entity.getMaterial(),
                entity.getDescription(),
                entity.getDifficulty(),
                entity.getDisintegration(),
                entity.getContaminationLevel(),
                FileMapper.convertToDto(entity.getImage())
        );
    }

}
