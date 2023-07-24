package com.shipmentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("http://localhost:9099")
    private String devUrl;

    @Bean
    public OpenAPI myAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("singh03ravi09@gmail.com");
        contact.setName("Ravi");

        Info info = new Info().title("Spring Boot Microservices API").version("1.0").contact(contact)

                .description("This API exposes endpoints to manage microservices.");

        return new OpenAPI().info(info).servers(List.of(devServer));

    }
}