package com.ozono.ia.controller;

import com.ozono.ia.dto.ResponseDto;
import com.ozono.ia.dto.UserAuthDto;
import com.ozono.ia.dto.UserRegisterDto;
import com.ozono.ia.services.AuthenticationService;
import com.ozono.ia.services.EmailConfirmationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ozono/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final EmailConfirmationService emailConfirmationService;


    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@Valid @RequestBody UserAuthDto dto) {
        return ResponseEntity.ok(authenticationService.authenticate(dto.email(), dto.password()));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody UserRegisterDto dto) {
        ResponseDto response = authenticationService.register(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/email-confirmation")
    public ResponseEntity<ResponseDto> emailConfirmation(@RequestParam Integer code) {
        emailConfirmationService.confirmEmail(code);
        return ResponseEntity.ok(ResponseDto.builder()
                .code(HttpStatus.OK.value())
                .message("Email confirmed")
                .phrase(HttpStatus.OK.name())
                .build());
    }

}
