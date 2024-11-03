package ru.prusakova.linkshortener.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.prusakova.linkshortener.dto.common.CommonResponse;
import ru.prusakova.linkshortener.dto.common.ValidationError;
import ru.prusakova.linkshortener.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class LinkShortenerExceptionHandler {

    private final String notFoundPage;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ValidationError> validationErrors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> ValidationError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();

        log.warn("Ошибка валидации: {}", validationErrors, e);

        return CommonResponse.builder()
                .errorMessage("Ошибка валидации")
                .validationErrors(validationErrors)
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        log.error("Невозможно открыть ссылку: {} ", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_HTML)
                .body(notFoundPage);
    }

    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e) {
        log.error("Непредвиденное исключение: {} ", e.getMessage(), e);

        return CommonResponse.builder()
                .id(UUID.randomUUID())
                .errorMessage("Непредвиденное исключение: " + e.getMessage())
                .build();
    }
}
