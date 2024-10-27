package ru.todo.java_todo_list.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "DTO простого ответа")
public class MessageResponse {

    @Schema(description = "Сообщение ответа")
    public Object message;

    @Schema(description = "Если есть ошибка, то указывается тело ошибки, в случае успеха, это поле отсутствует")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object error;

    public MessageResponse(Object message) {
        this.message = message;
    }
}
