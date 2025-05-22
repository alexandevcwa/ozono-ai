package com.ozono.ia.services;

import com.ozono.ia.client.*;
import com.ozono.ia.dto.AnalysisDto;
import com.ozono.ia.exception.ServiceException;
import com.ozono.ia.mapper.AnalysisMapper;
import com.ozono.ia.model.Analysis;
import com.ozono.ia.model.File;
import com.ozono.ia.repository.FileRepository;
import com.ozono.ia.repository.AnalysisRepository;
import com.ozono.ia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzerServiceImpl implements AnalyzerService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FtpClient ftpClient;
    private final CdnTokenGenerator cdnTokenGenerator;
    private final CdnMediaAccessClientImpl cdnMediaAccessClient;
    private final ImageAnalyzerAI imageAnalyzerAI;
    private final AnalysisRepository analysisRepository;


    @Value("${web.url}")
    private String webUrl;

    @Override
    public AnalysisDto analyzeImage(MultipartFile multipartFile) {

        File fileObj = saveFileInfo(multipartFile);
        try {
            ftpClient.storeFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
            String tempToken = cdnTokenGenerator.generateToken(24);
            cdnMediaAccessClient.addTemporaryToken(fileObj.getFileUuid(), tempToken);
            String cdnUrl = fileObj.getFileUrl() + "?token=" + tempToken;
            log.info(cdnUrl);
            return analyzeImage(cdnUrl, fileObj);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Error uploading file");
        }
    }

    private AnalysisDto analyzeImage(String url, File fileObj) {
        AnalysisDto dto = imageAnalyzerAI.analyze(url);
        Analysis analysis = AnalysisMapper.convertToEntity(dto);
        analysis.setUserId(fileObj.getOwner());
        analysis.setPhotoId(fileObj.getId());
        Analysis savedAnalysis = analysisRepository.save(analysis);
        savedAnalysis.setImage(fileObj);
        return AnalysisMapper.convertToDto(savedAnalysis);
    }

    /**
     * Save the file information in the database.
     *
     * @param file the file to save
     * @return the saved file object
     */
    private File saveFileInfo(MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getPrincipal().toString();
        String uuid = UUID.randomUUID().toString();
        File fileObj = File.builder()
                .filePath("/" + username)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .fileUrl(webUrl + "/ozono/cdn/pics/" + uuid)
                .owner(userRepository.findIdByUsername(username))
                .fileUuid(uuid)
                .fileExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1))
                .build();
        return fileRepository.saveAndFlush(fileObj);
    }

    @Override
    public Page<AnalysisDto> getAllAnalysisByUserAuthenticated() {
        return null;
    }
}
