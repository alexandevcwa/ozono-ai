package com.ozono.ia.conf;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class OpenAIConf {

    private final Environment environment;

    @Bean
    OpenAIClient openAIClient() {
        return OpenAIOkHttpClient.builder()
                .apiKey(Objects.requireNonNull(environment.getProperty("openai.api-key")))
                .organization(environment.getProperty("openai.organization"))
                .project(environment.getProperty("openai.project"))
                .baseUrl(Objects.requireNonNull(environment.getProperty("openai.base-url")))
                .build();
    }
}
