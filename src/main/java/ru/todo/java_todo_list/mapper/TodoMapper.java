package ru.todo.java_todo_list.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import ru.todo.java_todo_list.model.dto.TodoDto;
import ru.todo.java_todo_list.model.entity.TodoEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TodoMapper {

    TodoDto toDto(TodoEntity entity);

    TodoEntity toEntity(TodoDto dto);

    @Mapping(target = "creationDateTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isCompleted", ignore = true)
    TodoEntity updateEntityFromDto(TodoDto dto, @MappingTarget TodoEntity entity);
}
