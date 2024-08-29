package com.example.carrestservice.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url("http://localhost:8080")
                        )
                )
                .info(
                        new Info()
                                .title("Car Rest Service API - OpenAPI 3.0")
                                .description(
                                        "This is a sample Car Management Server based on the OpenAPI 3.0 specification. It offers comprehensive endpoints for managing cars, car models, categories, and manufacturers, providing a full suite of operations including creation, updating, retrieval, and deletion of records.\n" +
                                        "The API is designed to be intuitive, adhering to RESTful principles, which makes it simple to integrate with a wide range of clients. Whether you're building a web interface or a mobile application, this API offers the flexibility and reliability needed for seamless car management operations."
                                )
                );
    }



}
