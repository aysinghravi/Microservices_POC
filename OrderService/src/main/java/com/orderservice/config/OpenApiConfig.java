package com.orderservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("http://localhost:9097")
    private String devUrl;

    private static final String SCHEME_NAME = "bearerAuth";
    private static final String SCHEME = "bearer";

    @Bean
    public OpenAPI myAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("tanuja232002@gmail.com");
        contact.setName("TN456");

        Info info = new Info().title("Spring Boot Microservices API").version("1.0").contact(contact)

                .description("This API exposes endpoints to manage microservices.");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, createBearerScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .info(info)
                .servers(List.of(devServer))
                .addSecurityItem(new SecurityRequirement().addList("OAuth2Scheme"));
    }
    private SecurityScheme createBearerScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME);
    }
}


