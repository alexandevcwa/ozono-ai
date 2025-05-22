package com.ozono.ia.conf;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class OpenApiConf {

    private final Environment environment;

    private final static String SECURITY_SCHEMA_NAME = "bearerAuth";

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                        .title(environment.getProperty("springdoc.title"))
                        .description(environment.getProperty("springdoc.description"))
                        .version(environment.getProperty("springdoc.version")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEMA_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEMA_NAME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")));
    }
}
