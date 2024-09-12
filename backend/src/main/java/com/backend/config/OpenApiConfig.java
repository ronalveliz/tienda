package com.backend.config;


import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("bumerak")
                        .description("API Bumerak Stor X")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://www.example.com/")))
                .externalDocs(new ExternalDocumentation()
                        .description("Wiki Docs")
                        .url("https://www.example.com/"));
    }
}

