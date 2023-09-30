package com.example.backendchallenge.movie;

import com.example.backendchallenge.movie.dto.MovieDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    // Constructor to inject MovieService
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Endpoint to add a new Movie
    @PostMapping
    @Operation(summary = "Add Movie", description = "Add a new Movie", tags = {"Movie"})
    public ResponseEntity<Movie> createMovie(@RequestBody @NotNull MovieDTO movieDTO) {
        Movie newMovie = movieService.createMovie(movieDTO);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }

    // Endpoint to get all Movies
    @GetMapping
    @Operation(summary = "Get Movies", description = "Get all Movies", tags = {"Movie"})
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = this.movieService.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // Endpoint to get a Movie by its ID
    @GetMapping("/{movieId}")
    @Operation(summary = "Get a Movie", description = "Get a Movie by Id", tags = {"Movie"})
    public ResponseEntity<Movie> getMovieById(@PathVariable("movieId") UUID id) {
        Movie movie = this.movieService.getMovieById(id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    // Endpoint to update a Movie by its ID
    @PutMapping("/{movieId}")
    @Operation(summary = "Update a Movie", description = "Update a Movie by Id", tags = {"Movie"})
    public ResponseEntity<Movie> updateMovie(@PathVariable("movieId") UUID id, @RequestBody MovieDTO movieDTO) {
        Movie movie = this.movieService.updateMovie(id, movieDTO);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    // Endpoint to delete a Movie by its ID
    @DeleteMapping("/{movieId}")
    @Operation(summary = "Delete a Movie", description = "Delete a Movie by Id", tags = {"Movie"})
    public ResponseEntity<Void> deleteMovieById(@PathVariable("movieId") UUID id) {
        this.movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to get Movies filtered by launch date
    @GetMapping("/filters")
    @Operation(summary = "Get Movies", description = "Get Movies filtered by launch date", tags = {"Movie"})
    public ResponseEntity<List<Movie>> getFilteredMovies(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Start date in ISO 8601 format (YYYY-MM-DD)", required = true)
            LocalDate startDate,

            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "End date in ISO 8601 format (YYYY-MM-DD)", required = true)
            LocalDate endDate) {
        List<Movie> movies = this.movieService.getFilteredMovies(startDate, endDate);
        if(movies.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }
}
