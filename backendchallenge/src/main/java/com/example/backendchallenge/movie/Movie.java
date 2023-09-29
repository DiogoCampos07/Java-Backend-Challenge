package com.example.backendchallenge.movie;

import com.example.backendchallenge.movie.dto.MovieDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    @Column(name = "title", nullable = false)
    @Size(min = 2, max = 250, message = "Title must be between {min} and {max} characters long")
    private String title;
    @Column(name = "launch_date", nullable = false)
    private LocalDate launchDate;
    @Column(name = "rank", nullable = false)
    @Min(value = 0, message = "Rank must be greater than or equal to {value}")
    @Max(value = 10, message = "Rank must be less than or equal to {value}")
    private Integer rank;
    @Column(name = "revenue", nullable = false)
    private BigDecimal revenue;


    public Movie(MovieDTO data) {
        this.launchDate = data.launchDate();
        this.title = data.title();
        this.rank = data.rank();
        this.revenue = data.revenue();
    }
}
