package ru.todo.java_todo_list.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import ru.todo.java_todo_list.exception.ApiServiceException;
import ru.todo.java_todo_list.model.dto.MessageResponse;

@Slf4j
@RestControllerAdvice
public class ControllerHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;

    @ExceptionHandler(ApiServiceException.class)
    public final ResponseEntity<MessageResponse> handleApiServiceException(ApiServiceException e) {
        log.debug("Api Service exception: {}", e.getMessage());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(new MessageResponse("Error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> errorMap.put(((FieldError) error).getField(), error.getDefaultMessage()));
        
        log.debug("Method argument not valid error: {}", e.getAllErrors().stream().map(error -> "\n%s - %s".formatted(((FieldError) error).getField(), error.getDefaultMessage())).collect(Collectors.joining("; ")));
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", errorMap));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessageResponse> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getConstraintViolations().forEach(error -> errorMap.put(error.getPropertyPath().toString(), error.getMessage()));

        log.debug("Method argument not valid error: {}", e.getConstraintViolations().stream().map(error -> "\n%s - %s".formatted(error.getPropertyPath().toString(), error.getMessage())).collect(Collectors.joining("; ")));
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", errorMap));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.debug("Http message not readable error: {}", e.getMessage());
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", "Некорректное тело запроса"));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<MessageResponse> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        log.debug("Missing servlet request part error: {}", e.getMessage());
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", "Пропущен параметр в запросе"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MessageResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.debug("Http request method not supported: {}", e.getMessage());
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", "Не поддерживаемый метод запроса"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<MessageResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.debug("Missing servlet request parameter exception: {}", e.getMessage());
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", "Пропущен параметр в запросе"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<MessageResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.debug("Max upload size exceeded exception: {}", e.getMessage());
        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(new MessageResponse("Error", "Максимальный размер файла не должен превышать %s".formatted(MAX_FILE_SIZE)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.debug("Illegal Argument Exception: {}", e.getMessage());
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", "Некорректные параметры в запросе"));
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<MessageResponse> handlePropertyReferenceException(PropertyReferenceException e) {
        log.debug("Property Reference Exception: {}", e.getMessage());
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", "Некорректные параметры фильтрации"));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handlerHttpClientErrorException(HttpClientErrorException e) {
        log.debug("Http Client Error Exception: {}", e.getMessage());
        return ResponseEntity
            .status(e.getStatusCode())
            .body(e.getResponseBodyAsString());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<MessageResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.debug("Forbidden error: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new MessageResponse("Error", "Доступ запрещён"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<MessageResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.debug("Method Argument Type Mismatch Exception: {}", ex.getMessage());
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error", "Ошибка в параметрах запроса"));
    }

    @ApiResponse(responseCode = "500", description = "Ошибка на сервере", content = {
        @Content(schema = @Schema(implementation = MessageResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
            @ExampleObject(value = "{\"message\": \"Error\", \"error\": \"Произошла ошибка, попробуйте позже\"}")
        })
    })
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<MessageResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime error: {}", ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity
            .internalServerError()
            .body(new MessageResponse("Error", "Произошла ошибка, попробуйте позже"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<MessageResponse> handleAuthenticationException(AuthenticationException ex) {
        log.debug("Authentication error: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new MessageResponse("Error", "Неавторизованный доступ"));
    }

}
