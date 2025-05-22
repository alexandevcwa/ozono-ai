package com.ozono.ia.email;

import com.ozono.ia.conf.ThreadPoolConf;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegisteredEmail extends EmailCore {

    private final TemplateEngine templateEngine;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async(ThreadPoolConf.THREAD_POOL_NAME)
    public void sendEmailConfirmation(String to, int code) {

        Context context = new Context();
        context.setVariable("code", code);

        String subject = "Email Confirmation - Ozone IA - " + code;

        String html = templateEngine.process("ozono-email-confirmation", context);

        try {
            MimeMessageHelper helper = getMimeMessageHelper(mailSender);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(helper.getMimeMessage());
            log.info("Email confirmation sent to " + to);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }

    }
}
