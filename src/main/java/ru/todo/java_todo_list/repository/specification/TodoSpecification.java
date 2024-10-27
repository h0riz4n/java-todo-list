package ru.todo.java_todo_list.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import ru.todo.java_todo_list.model.entity.TodoEntity_;
import ru.todo.java_todo_list.model.filter.TodoFilterModel;
import ru.todo.java_todo_list.model.entity.TodoEntity;


@RequiredArgsConstructor
public class TodoSpecification implements Specification<TodoEntity>{

    private final TodoFilterModel filter;

    @Override
    public Predicate toPredicate(Root<TodoEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (filter == null)
            return null;

        List<Predicate> predicates = new ArrayList<>();

        filter.getPriority().ifPresent(priority -> {
            predicates.add(criteriaBuilder.equal(root.get(TodoEntity_.PRIORITY), priority));
        });

        if (predicates.isEmpty())
            return null;
        else
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
