package com.example.backendchallenge.movie;

import com.example.backendchallenge.exceptions.MovieNotFoundException;
import com.example.backendchallenge.movie.dto.MovieDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository repository;

    // Constructor to inject the MovieRepository
    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    // Validate a Movie entity using Bean Validation
    protected Boolean validateMovie(@NotNull Movie movie) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Movie> violation : violations) {
                errorMessage.append(violation.getMessage()).append(";");
            }
            throw new ValidationException(errorMessage.toString());
        }

        return true;
    }

    // Create a new Movie based on a MovieDTO
    public Movie createMovie(MovieDTO movieDTO) {
        Movie newMovie = new Movie(movieDTO);
        if (validateMovie(newMovie))
            repository.save(newMovie);
        return newMovie;
    }

    // Get a Movie by its UUID
    public Movie getMovieById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
    }

    // Get all Movies
    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    // Update a Movie based on its UUID and a MovieDTO
    public Movie updateMovie(UUID id, MovieDTO movieDTO) {
        Movie movie = repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        movie.setTitle(movieDTO.title());
        movie.setLaunchDate(movieDTO.launchDate());
        movie.setRank(movieDTO.rank());
        movie.setRevenue(movieDTO.revenue());
        if (validateMovie(movie))
            repository.save(movie);
        return movie;
    }

    // Delete a Movie by its UUID
    public void deleteById(UUID id) {
        repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        repository.deleteById(id);
    }

    // Get filtered Movies by start and end dates
    public List<Movie> getFilteredMovies(LocalDate startDate, LocalDate endDate) {
        return repository.getFilteredMovies(startDate, endDate);
    }

}
