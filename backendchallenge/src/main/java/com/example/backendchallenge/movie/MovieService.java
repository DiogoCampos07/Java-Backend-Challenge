package com.example.backendchallenge.movie;

import com.example.backendchallenge.exceptions.MovieNotFoundException;
import com.example.backendchallenge.movie.dto.MovieDTO;
import jakarta.validation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    private void validateAndSaveMovie(Movie movie) {
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

        repository.save(movie);
    }

    public Movie createMovie(MovieDTO movieDTO) {
        Movie newMovie = new Movie(movieDTO);
        validateAndSaveMovie(newMovie);
        return newMovie;
    }

    public Movie getMovieById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
    }

    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    public Movie updateMovie(UUID id, MovieDTO movieDTO) {
        Movie movie = repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        movie.setTitle(movieDTO.title());
        movie.setLaunchDate(movieDTO.launchDate());
        movie.setRank(movieDTO.rank());
        movie.setRevenue(movieDTO.revenue());
        validateAndSaveMovie(movie);
        repository.save(movie);
        return movie;
    }

    public void deleteById(UUID id) {
        repository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        repository.deleteById(id);
    }

    public List<Movie> getFilteredMovies(LocalDate startDate, LocalDate endDate) {
        return repository.getFilteredMovies(startDate, endDate);
    }

}
