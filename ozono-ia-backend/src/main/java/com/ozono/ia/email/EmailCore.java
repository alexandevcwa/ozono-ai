package com.ozono.ia.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public abstract class EmailCore {

    public MimeMessageHelper getMimeMessageHelper(JavaMailSender mailSender) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setHeader("X-Mailer", "GCGADSS Mailer");
        message.setHeader("X-Priority", "3");
        message.setHeader("X-MSMail-Priority", "Normal");
        message.setHeader("Importance", "Normal");
        message.setHeader("Content-Type", "text/html; charset=UTF-8");
        message.setHeader("Content-Transfer-Encoding", "8bit");
        return new MimeMessageHelper(message, true, "UTF-8");
    }
}
