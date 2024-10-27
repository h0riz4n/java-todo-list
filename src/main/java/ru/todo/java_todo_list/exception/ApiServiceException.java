package ru.todo.java_todo_list.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiServiceException extends RuntimeException {

    private HttpStatus httpStatus;

    public ApiServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}

