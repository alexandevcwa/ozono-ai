package com.ozono.ia.controller;

import com.ozono.ia.dto.ResponseDto;
import com.ozono.ia.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseDto> handleServiceException(ServiceException e) {
        return ResponseEntity.status(e.getStatus())
                .body(ResponseDto.builder()
                        .message(e.getMessage())
                        .code(e.getStatus().value())
                        .phrase(e.getStatus().name())
                        .extra(e.getExtra())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleException(Exception e) {
        return ResponseEntity.status(500)
                .body(ResponseDto.builder()
                        .message(e.getMessage())
                        .code(500)
                        .phrase("Internal Server Error")
                        .build());
    }

}
