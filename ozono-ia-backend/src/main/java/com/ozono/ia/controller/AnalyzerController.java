package com.ozono.ia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.ozono.ia.dto.AnalysisDto;
import com.ozono.ia.services.AnalyzerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ozono/api/v1/analyzer")
public class AnalyzerController {

    private final AnalyzerService analyzerService;

    public AnalyzerController(
            @Qualifier("analyzerProxy")
            AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnalysisDto> analyzeImageWithAI(
            @Parameter(description = "Image to analyze", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("image") MultipartFile image
    ) {
        return ResponseEntity.ok(analyzerService.analyzeImage(image));
    }


}
