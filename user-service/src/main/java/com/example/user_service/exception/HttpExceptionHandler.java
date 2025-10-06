package com.example.user_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.Map;

@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Object> httpException(HttpException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.getStatus()).body(Map.of(
                "timestamp", ZonedDateTime.now(),
                "status", ex.getStatus().value(),
                "error", ex.getMessage(),
                "path", request.getRequestURI()
        ));
    }

}