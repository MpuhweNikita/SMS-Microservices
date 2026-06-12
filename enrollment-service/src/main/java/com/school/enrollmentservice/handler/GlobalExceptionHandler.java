package com.school.enrollmentservice.handler;

import com.school.enrollmentservice.dto.ErrorResponse;
import com.school.enrollmentservice.exception.DuplicateResourceException;
import com.school.enrollmentservice.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Mono<ErrorResponse>> handleResourceNotFound(ResourceNotFoundException ex, ServerWebExchange exchange) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Mono.just(buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), exchange)));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Mono<ErrorResponse>> handleDuplicateResource(DuplicateResourceException ex, ServerWebExchange exchange) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Mono.just(buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), exchange)));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ErrorResponse>> handleValidationException(WebExchangeBindException ex, ServerWebExchange exchange) {
        String details = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Mono.just(buildResponse(HttpStatus.BAD_REQUEST, "Validation failed: " + details, exchange)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Mono<ErrorResponse>> handleGeneralException(Exception ex, ServerWebExchange exchange) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), exchange)));
    }

    private ErrorResponse buildResponse(HttpStatus status, String message, ServerWebExchange exchange) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(exchange.getRequest().getPath().value())
                .build();
    }
}
