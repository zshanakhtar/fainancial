package com.fainancial.KnowledgeBuilder.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Fainance API Documentations",
                version = "1.0",
                description = "API documentation for FAInance application"
        ))
@Configuration
public class OpenApiConfig {
}
