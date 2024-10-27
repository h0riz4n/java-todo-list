package ru.todo.java_todo_list.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ru.todo.java_todo_list.model.entity.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Integer>, JpaSpecificationExecutor<TodoEntity> {

    List<TodoEntity> findAll(Specification<TodoEntity> specification);
}
