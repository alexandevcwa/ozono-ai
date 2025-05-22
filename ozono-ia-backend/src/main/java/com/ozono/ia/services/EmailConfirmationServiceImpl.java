package com.ozono.ia.services;

import com.ozono.ia.event.UserEmailConfirmedEvent;
import com.ozono.ia.exception.ServiceException;
import com.ozono.ia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private static final Map<String, Integer> emailConfirmation = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;


    @Override
    public void confirmEmail(Integer code) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getPrincipal().toString();

        Integer currentCode = emailConfirmation.get(username);

        if (currentCode == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "No confirmation code found");
        }

        if (currentCode.equals(code)) {
            emailConfirmation.remove(username);

            userRepository.findByUsername(username).ifPresent(user -> {
                user.setEmailConfirmed("Y");
                userRepository.saveAndFlush(user);
                applicationEventPublisher.publishEvent(new UserEmailConfirmedEvent(this,user.getUsername(), user.getEmail(),user.getUpdatedAt()));
            });

        } else {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Incorrect confirmation code");
        }
    }

    @Override
    public void addConfirmationCode(String username, Integer code) {
        emailConfirmation.put(username, code);
    }
}
