package com.ozono.ia.dto;

import lombok.*;

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
}
