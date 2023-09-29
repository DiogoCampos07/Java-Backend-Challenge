package com.example.backendchallenge.movie.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovieDTO(
        String title,
        LocalDate launchDate,
        Integer rank,
        BigDecimal revenue) {
}
