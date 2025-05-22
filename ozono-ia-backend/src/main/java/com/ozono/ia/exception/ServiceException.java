package com.ozono.ia.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String extra;

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.extra = null;
    }

    public ServiceException(HttpStatus status, String message, String extra) {
        super(message);
        this.status = status;
        this.extra = extra;
    }

}
