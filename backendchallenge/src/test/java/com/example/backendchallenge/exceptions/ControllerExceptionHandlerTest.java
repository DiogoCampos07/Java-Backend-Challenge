package com.example.backendchallenge.exceptions;

import jakarta.xml.bind.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerExceptionHandlerTest {

    @Test
    public void testHandleEntityNotFoundException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ResponseEntity<?> responseEntity = handler.threat404();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleValidationException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        ValidationException exception = new ValidationException("Validation failed");
        ResponseEntity<?> responseEntity = handler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleMovieNotFoundException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        UUID movieId = UUID.randomUUID();
        MovieNotFoundException exception = new MovieNotFoundException(movieId);
        ResponseEntity<?> responseEntity = handler.handleMovieNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleGeneralException() {
        ControllerExceptionHandler handler = new ControllerExceptionHandler();
        Exception exception = new Exception("Internal Server Error");
        ResponseEntity<?> responseEntity = handler.threatGeneralException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}