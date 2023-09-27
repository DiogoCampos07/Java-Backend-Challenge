package com.example.backendchallenge.movie;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "movies")
@Table(name = "movies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Movie {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "title")
    @Size(min = 1, max = 250, message = "Title must not exceeded {max} characters long")
    private String title;
    @Column(name = "launchDate", nullable = false)
    private LocalDateTime launchDate;
    @Column(name = "rank")
    @Size(max = 10, message = "Rank must be between 0 and {max}")
    private Integer rank;
    @Column(name = "revenue")
    private BigDecimal revenue;

}
