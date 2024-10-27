package ru.todo.java_todo_list.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.todo.java_todo_list.exception.ApiServiceException;
import ru.todo.java_todo_list.mapper.TodoMapper;
import ru.todo.java_todo_list.model.dto.TodoDto;
import ru.todo.java_todo_list.model.entity.TodoEntity;
import ru.todo.java_todo_list.model.filter.TodoFilterModel;
import ru.todo.java_todo_list.repository.TodoRepository;
import ru.todo.java_todo_list.repository.specification.TodoSpecification;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoMapper todoMapper;
    private final TodoRepository todoRepo;

    public List<TodoEntity> getAll(TodoFilterModel filter) {
        return todoRepo.findAll(new TodoSpecification(filter));
    }

    @Transactional
    public TodoEntity create(TodoEntity todo) {
        return todoRepo.save(todo);
    }

    public TodoEntity getById(Integer id) {
        return todoRepo.findById(id)
            .orElseThrow(() -> new ApiServiceException("TODO не найдено", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public TodoEntity edit(Integer id, TodoDto dto) {
        TodoEntity todo = todoRepo.findById(id)
            .orElseThrow(() -> new ApiServiceException("TODO не найдено", HttpStatus.NOT_FOUND));
        return todoRepo.save(todoMapper.updateEntityFromDto(dto, todo));
    }

    @Transactional
    public void deleteById(Integer id) {
        TodoEntity todo = todoRepo.findById(id)
            .orElseThrow(() -> new ApiServiceException("TODO не найдено", HttpStatus.NOT_FOUND));
        todoRepo.delete(todo);
    }

    @Transactional
    public TodoEntity editIsCompleted(Integer id) {
        TodoEntity todo = todoRepo.findById(id)
            .orElseThrow(() -> new ApiServiceException("TODO не найдено", HttpStatus.NOT_FOUND));
        
        todo.setIsCompleted(!todo.getIsCompleted());
        return todoRepo.save(todo);
    }
}
