package com.cyrcetech.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cyrcetechOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cyrcetech API")
                        .description("REST API para el sistema de gestión de taller de reparación Cyrcetech")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Cyrcetech")
                                .email("contacto@cyrcetech.com"))
                        .license(new License()
                                .name("Private")
                                .url("https://cyrcetech.com")));
    }
}
