package com.ozono.ia.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private int code;
    private String message;
    private String phrase;
    private String extra;
    private LocalDateTime date;
}
