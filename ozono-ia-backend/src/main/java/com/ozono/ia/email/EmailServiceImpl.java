package com.ozono.ia.email;

import com.ozono.ia.conf.ThreadPoolConf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl extends EmailCore implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async(ThreadPoolConf.THREAD_POOL_NAME)
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessageHelper helper = getMimeMessageHelper(mailSender);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(helper.getMimeMessage());

            log.info("Email sent to {} with subject {} ", to, subject);
        } catch (Exception e) {
            String message = String.format("Error to send email to %s with subject %s",to,subject);
            log.error(message, e);
        }

    }
}
