package com.ozono.ia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnalysisDto(

        @JsonProperty("analysis_id")
        Long analysisId,

        @JsonProperty("material_type")
        String materialType,

        @JsonProperty("material_description")
        String materialDescription,

        @JsonProperty("difficulty_of_recycle")
        String difficultyOfRecycle,

        @JsonProperty("disintegration_time")
        String disintegrationTime,

        @JsonProperty("contamination_level")
        String contaminationLevel,

        @JsonProperty("image_content")
        FileDto image
) {
}