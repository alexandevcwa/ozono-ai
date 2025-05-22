package com.ozono.ia.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class UserEmailConfirmedEvent extends ApplicationEvent {

    private final String username;
    private final String email;
    private final LocalDateTime confirmed;

    public UserEmailConfirmedEvent(Object source, String username, String email, LocalDateTime confirmed) {
        super(source);
        this.username = username;
        this.email = email;
        this.confirmed = confirmed;
    }
}
