package com.ozono.ia.services.proxy;

import com.ozono.ia.dto.AnalysisDto;
import com.ozono.ia.exception.ServiceException;
import com.ozono.ia.services.AnalyzerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service("analyzerProxy")
public class AnalyzerProxy implements AnalyzerService {

    private final AnalyzerService analyzerService;
    private final Tika tika;

    private static final List<String> mimeTypes = List.of(
            "image/jpeg",
            "image/png",
            "image/webp");

    public AnalyzerProxy(@Qualifier("analyzerServiceImpl") AnalyzerService analyzerService, Tika tika) {
        this.analyzerService = analyzerService;
        this.tika = tika;
    }


    @Override
    public AnalysisDto analyzeImage(MultipartFile file) {
        validateMimeType(file);
        return analyzerService.analyzeImage(file);
    }

    @Override
    public Page<AnalysisDto> getAllAnalysisByUserAuthenticated() {
        return null;
    }

    private void validateMimeType(MultipartFile file) {
        try {
            String mimeType = tika.detect(file.getInputStream());
            if (!mimeTypes.contains(mimeType)) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "Invalid file type");
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Error reading file");
        }
    }
}
