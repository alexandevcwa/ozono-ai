package com.ozono.ia.listener;

import com.ozono.ia.dto.AnalysisDto;
import com.ozono.ia.email.EmailService;
import com.ozono.ia.event.AnalysisCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisListener {

    private final TemplateEngine templateEngine;
    private final EmailService emailService;

    @EventListener
    public void onAnalysisIsCompleted(AnalysisCompletedEvent event){
        Context context = new Context();
        context.setVariable("no_analysis", event.getAnalysis().analysisId());
        context.setVariable("material",event.getAnalysis().materialType());
        context.setVariable("description", event.getAnalysis().materialDescription());
        context.setVariable("difficulty", event.getAnalysis().difficultyOfRecycle());
        context.setVariable("disintegration", event.getAnalysis().disintegrationTime());
        context.setVariable("level", event.getAnalysis().contaminationLevel());
        context.setVariable("file_name",event.getAnalysis().image().fileName());
        context.setVariable("uuid", event.getAnalysis().image().fileUuid());
        String html = templateEngine.process("ozono-email-analysis", context);
        emailService.sendEmail(event.getEmail(), "Analysis Completed - Ozono IA - " + event.getAnalysis().analysisId(), html);
    }
}
