package com.example.backendchallenge.movie.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovieDTO(
        LocalDate launchDate,
        String title,
        Integer rank,
        BigDecimal revenue) {
}
