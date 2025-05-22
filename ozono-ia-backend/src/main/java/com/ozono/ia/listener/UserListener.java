package com.ozono.ia.listener;

import com.ozono.ia.email.UserRegisteredEmail;
import com.ozono.ia.event.UserRegisteredEvent;
import com.ozono.ia.services.EmailConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserListener {

    private final UserRegisteredEmail userRegisteredEmail;
    private final Random rand = new Random();
    private final EmailConfirmationService emailConfirmationService;

    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        int randomNumber = rand.nextInt(999999);
        emailConfirmationService.addConfirmationCode(event.getUsername(), randomNumber);
        userRegisteredEmail.sendEmailConfirmation(event.getEmail(), randomNumber);
    }
}
