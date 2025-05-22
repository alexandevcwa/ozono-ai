package com.ozono.ia.services;

import com.ozono.ia.client.FtpClientImpl;
import com.ozono.ia.dto.ResponseDto;
import com.ozono.ia.dto.UserRegisterDto;
import com.ozono.ia.event.UserRegisteredEvent;
import com.ozono.ia.exception.ServiceException;
import com.ozono.ia.mapper.UserRegisterMapper;
import com.ozono.ia.model.User;
import com.ozono.ia.repository.UserRepository;
import com.ozono.ia.security.JwtSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtSecurityService jwtSecurityService;
    private final ApplicationEventPublisher eventPublisher;
    private final FtpClientImpl ftpClientImpl;

    @Override
    public String authenticate(String email, String password) {

        User user = userRepository.findByEmail(email.toLowerCase()).orElseThrow(() ->
                new ServiceException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if ("N".equalsIgnoreCase(user.getEmailConfirmed())){
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Email not confirmed");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Invalid email or password","COMFIRM_EMAIL");
        }

        return jwtSecurityService.generateToken(user);

    }

    @Override
    public ResponseDto register(UserRegisterDto dto) {

        if (userRepository.existsByEmail(dto.email())){
            throw new ServiceException(HttpStatus.CONFLICT, "Email already in use");
        }
        String username;
        do {
            username = generateUsername(dto.firstName(), dto.lastName());
        }while (userRepository.existsByUsername(username));

        User user = UserRegisterMapper.convertToEntity(dto);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setStatus("E");
        user.setEmailConfirmed("N");
        user.setCredit(3);
        userRepository.save(user);

        String token = jwtSecurityService.generateToken(user);

        // Create a directory for the user in the FTP server
        ftpClientImpl.createDirectory(username);

        eventPublisher.publishEvent(new UserRegisteredEvent(this, username, dto.email()));

        return ResponseDto.builder()
                .code(HttpStatus.CREATED.value())
                .phrase(HttpStatus.CREATED.getReasonPhrase())
                .message("User registered successfully")
                .extra(token)
                .build();
    }

    private String generateUsername(String firstName, String lastName){
        String first = firstName.trim().toLowerCase();
        String last = lastName.trim().toLowerCase();
        String part1 = first.length() >= 3 ? first.substring(0, 3) : first;
        String part2 = last.length() >= 3 ? last.substring(0, 3) : last;
        int number = new Random().nextInt(900) + 100;
        return part1 + part2 + number;
    }
}
