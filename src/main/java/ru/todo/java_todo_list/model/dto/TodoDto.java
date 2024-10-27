package ru.todo.java_todo_list.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.todo.java_todo_list.model.type.EPriorityType;
import ru.todo.java_todo_list.model.view.TodoView;

@Getter
@Setter
public class TodoDto {

    @JsonView({
        TodoView.Summary.class, TodoView.Detail.class
    })
    private Integer id;

    @NotBlank
    @JsonView({
        TodoView.Summary.class, TodoView.Create.class, TodoView.Edit.class
    })
    @Schema(example = "Сходить на тренировку")
    private String title;

    @JsonView({
        TodoView.Summary.class, TodoView.Detail.class, TodoView.Create.class, TodoView.Edit.class
    })
    @Schema(example = "Тренировка в спортзале")
    private String description;

    @JsonView({
        TodoView.Summary.class, TodoView.Detail.class
    })
    @Schema(example = "false")
    private Boolean isCompleted;

    @NotNull
    @JsonView({
        TodoView.Summary.class, TodoView.Detail.class, TodoView.Create.class, TodoView.Edit.class
    })
    private EPriorityType priority;

    @JsonView({
        TodoView.Summary.class, TodoView.Detail.class
    })
    private LocalDateTime creationDateTime;
}
