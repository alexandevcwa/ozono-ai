package com.ozono.ia.services;

import com.ozono.ia.dto.AnalysisDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface AnalyzerService {

    /**
     * Analyze the object in the picture and return the analysis.
     *
     * @param file the picture file to analyze
     * @return the analysis result
     */
    AnalysisDto analyzeImage(MultipartFile file);

    /**
     * Get all analysis made by the authenticated user.
     *
     * @return a page of analysis results
     */
    Page<AnalysisDto> getAllAnalysisByUserAuthenticated();

}
