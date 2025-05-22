package com.ozono.ia.client;

import com.ozono.ia.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FtpClientImpl implements FtpClient {

    @Value("${ftp.client.host}")
    private String ftpHost;

    @Value("${ftp.client.port}")
    private Integer ftpPort;

    @Value("${ftp.client.user}")
    private String ftpUser;

    @Value("${ftp.client.password}")
    private String ftpPassword;

    @Override
    public void createDirectory(String directoryName) {
        FTPClient ftpClient = connect();
        try {
            ftpClient.changeWorkingDirectory("/");
            if (ftpClient.makeDirectory("/" + directoryName)) {
                log.info("Directory {} created", directoryName);
            } else {
                int code = ftpClient.getReplyCode();
                log.warn("Directory {} may already exist or couldn't be created, reply code {}", directoryName, code);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory", e);
        } finally {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("Error disconnecting from FTP server", e);
            }
        }
    }

    @Override
    public void storeFile(InputStream inputStream, String fileName) {
        FTPClient ftpClient = connect();
        try {
            String username = getUsernameAsDirectoryName();
            ftpClient.changeWorkingDirectory("/" + username);
            boolean success = ftpClient.storeFile(fileName, inputStream);
            inputStream.close();

            if (success) {
                log.info("File {} uploaded to {}", fileName, username);
            } else {
                throw new RuntimeException("Failed to upload file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        } finally {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("Error disconnecting from FTP server", e);
            }
        }
    }


    private FTPClient connect() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpHost, ftpPort);
        } catch (IOException e) {
            log.error("Error connecting to FTP server", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "FTP server refused connection");
        }

        int replyCode = ftpClient.getReplyCode();
        System.out.println("FTP Server Reply: " + replyCode + " - " + ftpClient.getReplyString());

        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "FTP server refused connection");
        }

        ftpClient.setAutodetectUTF8(true);
        ftpClient.setControlEncoding("UTF-8");
        boolean login;

        try {
            login = ftpClient.login(ftpUser, ftpPassword);
        } catch (IOException e) {
            log.error("Error logging in to FTP server", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "FTP server refused connection");
        }

        log.info("FTP Server Login: {}", login);
        if (!login) {
            throw new RuntimeException("Failed to login to FTP server.");
        }

        ftpClient.enterLocalPassiveMode();
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            log.error("Error setting file type", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "FTP server refused connection");
        }

        return ftpClient;
    }

    @Override
    public Map<String, Object> download(String filePath, String fileName) {
        FTPClient ftpClient = connect();

        try {
            ftpClient.changeWorkingDirectory(filePath);
        } catch (IOException e) {
            log.error("Error changing working directory", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "FTP server refused connection");
        }


        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            boolean success = ftpClient.retrieveFile(filePath + "/" + fileName, outputStream);

            if (!success || ftpClient.getReplyCode() == 550) {
                log.error("Failed to retrieve file: {}", fileName);
                throw new ServiceException(HttpStatus.NOT_FOUND, "File not found");
            }
            byte[] bytes = outputStream.toByteArray();

            Map<String, Object> result = new HashMap<>();
            result.put("input_stream", new ByteArrayResource(bytes));
            return result;

        } catch (IOException e) {
            log.error("Error retrieving file", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "FTP server refused connection");
        } finally {
            disconnectQuietly(ftpClient);
        }
    }

    private String getUsernameAsDirectoryName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal().toString();
    }

    /**
     * Disconnects the FTP client quietly.
     *
     * @param ftpClient the FTP client to disconnect
     */
    private void disconnectQuietly(FTPClient ftpClient) {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            log.warn("Error disconnecting from FTP server", e);
        }
    }

}
