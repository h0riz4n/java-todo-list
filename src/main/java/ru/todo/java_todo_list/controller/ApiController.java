package ru.todo.java_todo_list.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.todo.java_todo_list.mapper.TodoMapper;
import ru.todo.java_todo_list.model.dto.MessageResponse;
import ru.todo.java_todo_list.model.dto.TodoDto;
import ru.todo.java_todo_list.model.filter.TodoFilterModel;
import ru.todo.java_todo_list.model.type.EPriorityType;
import ru.todo.java_todo_list.model.view.TodoView;
import ru.todo.java_todo_list.service.TodoService;

@Tag(name = "API Контроллер")
@RestController
@Validated
@RequestMapping(path = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ApiController {

    private final TodoMapper todoMapper;
    private final TodoService todoService;

    @Operation(summary = "Получение TODO по ID")
    @GetMapping("/{id}")
    @JsonView(TodoView.Detail.class)
    public ResponseEntity<TodoDto> getById(
        @PathVariable("id") Integer id
    ) {
        return ResponseEntity.ok(todoMapper.toDto(todoService.getById(id)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Создание TODO",
        responses = {
            @ApiResponse(responseCode = "404", description = "НЕ НАЙДЕН", content = @Content(
                examples = {
                    @ExampleObject(name = "TODO не найдено", value = "{\"message\": \"Error\", \"error\": \"TODO не найдено\"}")
                }
            )),
            @ApiResponse(responseCode = "201", description = "СОЗДАН", content= @Content(
                examples = {
                    @ExampleObject(name = "TODO создан", value = "{\"message\": \"1\"}")
                }
            ))
        }
    )
    public ResponseEntity<MessageResponse> create(
        @RequestBody @JsonView(TodoView.Create.class) @Valid TodoDto dto
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new MessageResponse(todoService.create(todoMapper.toEntity(dto)).getId()));
    }

    @Operation(
        summary = "Удаление TODO по ID",
        responses = {
            @ApiResponse(responseCode = "404", description = "НЕ НАЙДЕН", content = @Content(
                examples = {
                    @ExampleObject(name = "TODO не найдено", value = "{\"message\": \"Error\", \"error\": \"TODO не найдено\"}")
                }
            )),
            @ApiResponse(responseCode = "204", description = "БЕЗ КОНТЕНТА")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable("id") Integer id
    ) { 
        todoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Получение всех TODO")
    @GetMapping("/all")
    @JsonView(TodoView.Summary.class)
    public ResponseEntity<List<TodoDto>> getAll(
        @RequestParam(name = "priority", required = false) EPriorityType priority
    ) {
        return ResponseEntity.ok(
            todoService
                .getAll(new TodoFilterModel(priority))
                .stream()
                .map(todo -> todoMapper.toDto(todo))
                .toList()
        );
    }

    @Operation(
        summary = "Изменения статуса завершения TODO",
        responses = {
            @ApiResponse(responseCode = "404", description = "НЕ НАЙДЕН", content = @Content(
                examples = {
                    @ExampleObject(name = "TODO не найдено", value = "{\"message\": \"Error\", \"error\": \"TODO не найдено\"}")
                }
            )),
            @ApiResponse(responseCode = "201", description = "СОЗДАН", content= @Content(
                examples = {
                    @ExampleObject(name = "TODO создан", value = "{\"message\": \"true\"}")
                }
            ))
        }
    )
    @PutMapping("/{id}/is-completed")
    public ResponseEntity<MessageResponse> editIsCompleted(
        @PathVariable("id") Integer id
    ) {
        return ResponseEntity.ok(new MessageResponse(todoService.editIsCompleted(id).getIsCompleted()));
    }

    @Operation(
        summary = "Изменение TODO",
        responses = {
            @ApiResponse(responseCode = "404", description = "НЕ НАЙДЕН", content = @Content(
                examples = {
                    @ExampleObject(name = "TODO не найдено", value = "{\"message\": \"Error\", \"error\": \"TODO не найдено\"}")
                }
            )),
            @ApiResponse(responseCode = "201", description = "СОЗДАН", content= @Content(
                examples = {
                    @ExampleObject(name = "TODO создан", value = "{\"message\": \"1\"}")
                }
            ))
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> edit(
        @PathVariable("id") Integer id,
        @RequestBody @JsonView(TodoView.Edit.class) @Valid TodoDto dto
    ) {
        return ResponseEntity.ok(new MessageResponse(todoService.edit(id, dto).getId()));
    }
}
