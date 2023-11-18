package ru.itmo.precrimeupd.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "PreCrime System Api",
                description = "PreCrime System", version = "1.0.0",
                contact = @Contact(
                        name = "Kiryushin Vitaliy",
                        email = "kiryushcin.vitalij@yandex.ru"
                )
        )
)
public class OpenApiConfig {
}
