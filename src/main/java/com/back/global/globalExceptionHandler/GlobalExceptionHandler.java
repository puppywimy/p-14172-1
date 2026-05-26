package com.back.global.globalExceptionHandler;

import com.back.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public RsData<Void> handle(NoSuchElementException exception) {
        return new RsData<>(
                "404-1",
                "해당 데이터가 존재하지 않습니다."
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RsData<Void> handle(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getAllErrors().stream()
                .filter((error) -> error instanceof FieldError)
                .map((error) -> (FieldError) error)
                .map((error) -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("\n"));

        return new RsData<>(
                "400-1",
                message
        );
    }
}