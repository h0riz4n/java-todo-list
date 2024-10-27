package ru.todo.java_todo_list.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "java.swagger")
public class SwaggerProperty {

    private Info info;
    private Server server;

    @Getter
    @Setter
    public static class Info {

        private String title;
        private String version;
        private String description;
    }

    @Getter
    @Setter
    public static class Server {

        private String url;
        private String description;
    }

}
