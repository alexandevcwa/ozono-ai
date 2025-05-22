package com.ozono.ia.client;

import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Map;

public interface FtpClient {

    void createDirectory(String directoryName);

    void storeFile(InputStream inputStream, String fileName);

    /**
     * Download a file from the FTP server
     *
     * @param filePath File path on the server
     * @param fileName File name to be saved
     * @return A map containing the FTP Client and Stream
     */
    Map<String, Object> download(String filePath, String fileName);
}
