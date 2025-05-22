package com.ozono.ia.email;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
