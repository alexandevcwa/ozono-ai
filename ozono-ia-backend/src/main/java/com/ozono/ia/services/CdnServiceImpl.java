package com.ozono.ia.services;

import com.ozono.ia.client.CdnMediaAccessClient;
import com.ozono.ia.client.FtpClient;
import com.ozono.ia.exception.ServiceException;
import com.ozono.ia.model.File;
import com.ozono.ia.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CdnServiceImpl implements CdnService {

    private final FileRepository fileRepository;
    private final CdnMediaAccessClient cdnMediaAccessClient;
    private final FtpClient ftpClient;

    @Override
    public Map<String, Object> getCdnFileContextWithToken(String uuid, String token) {

        log.info("Open AI is requesting file with uuid: {}", uuid);

        boolean isValid = cdnMediaAccessClient.isTokenValid(uuid, token);
        if (isValid) {
            File file = fileRepository.findByFileUuid(uuid);
            Map<String, Object> map = ftpClient.download(file.getFilePath(), file.getFileName());
            map.put("content_type", file.getFileType());
            return map;
        }
        throw new ServiceException(HttpStatus.UNAUTHORIZED, "Invalid token to access the file");
    }
}
