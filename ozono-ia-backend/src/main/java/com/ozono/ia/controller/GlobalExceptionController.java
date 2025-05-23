package com.ozono.ia.controller;

import com.ozono.ia.dto.ResponseDto;
import com.ozono.ia.exception.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {


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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
        String message = validationErrorList.get(0).getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDto.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .phrase(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(message)
                        .date(LocalDateTime.now())
                        .build());
    }
}
