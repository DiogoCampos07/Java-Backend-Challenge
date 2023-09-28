package com.example.backendchallenge.exceptions;

import java.util.UUID;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(UUID id) {
        super("Movie with ID " + id + " not found");
    }
}

