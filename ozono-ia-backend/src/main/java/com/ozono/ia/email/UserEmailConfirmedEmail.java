package com.ozono.ia.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
public class UserEmailConfirmedEmail extends EmailCore{



    private final JavaMailSender mailSender;



}
