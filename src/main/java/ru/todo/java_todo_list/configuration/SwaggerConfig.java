package ru.todo.java_todo_list.configuration;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;

import ru.todo.java_todo_list.property.SwaggerProperty;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final SwaggerProperty swaggerProperty;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title(new String(swaggerProperty.getInfo().getTitle().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8))
                    .version(swaggerProperty.getInfo().getVersion())
                    .description(new String(swaggerProperty.getInfo().getDescription().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8))
            )
            .servers(
                List.of(
                    new Server()
                        .url(swaggerProperty.getServer().getUrl())
                        .description(new String(swaggerProperty.getServer().getDescription().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8))
                )
            );
    }

}
