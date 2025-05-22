package com.ozono.ia.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
@RequiredArgsConstructor
public class EmailService extends EmailCore {

    private final JavaMailSender mailSender;


    public Boolean sendEmail(String from, String to, String subject, String body) {
        try {
            MimeMessageHelper helper = getMimeMessageHelper(mailSender);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(helper.getMimeMessage());

            log.info("Email sent to {} with subject {} ", to, subject);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }
}
