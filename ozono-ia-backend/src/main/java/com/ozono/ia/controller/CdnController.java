package com.ozono.ia.controller;

import com.ozono.ia.services.CdnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ozono/cdn")
public class CdnController {

    private final CdnService cdnService;


    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuid, @RequestParam("token") String token) {

        Map<String, Object> map = cdnService.getCdnFileContextWithToken(uuid, token);
        Resource inputStream = (Resource) map.get("input_stream");
        String contentType = (String) map.get("content_type");

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(inputStream);
    }
}
