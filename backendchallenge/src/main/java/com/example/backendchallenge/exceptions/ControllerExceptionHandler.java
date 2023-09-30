package com.example.backendchallenge.exceptions;

import com.example.backendchallenge.exceptions.dto.ExceptionDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    // Exception handler for EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> threat404(){
        return ResponseEntity.notFound().build();
    }

    // Exception handler for ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException exception) {
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), "400");
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    // Exception handler for MovieNotFoundException
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<?> handleMovieNotFoundException(MovieNotFoundException exception) {
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), "404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDto);
    }

    // Generic exception handler for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> threatGeneralException(Exception exception){
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), "500");
        return ResponseEntity.internalServerError().body(exceptionDto);    }
}
