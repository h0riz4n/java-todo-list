package ru.todo.java_todo_list.model.filter;

import java.util.Optional;

import lombok.AllArgsConstructor;
import ru.todo.java_todo_list.model.type.EPriorityType;

@AllArgsConstructor
public class TodoFilterModel {

    private EPriorityType priority;

    public Optional<EPriorityType> getPriority() {
        return Optional.ofNullable(this.priority);
    }
}
