package com.ozono.ia.event;

import com.ozono.ia.dto.AnalysisDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AnalysisCompletedEvent extends ApplicationEvent {

    private final String email;
    private final AnalysisDto analysis;

    public AnalysisCompletedEvent(Object source, String email, AnalysisDto analysis) {
        super(source);
        this.email = email;
        this.analysis = analysis;
    }
}
