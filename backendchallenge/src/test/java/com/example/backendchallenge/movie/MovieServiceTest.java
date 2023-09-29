package com.example.backendchallenge.movie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.backendchallenge.exceptions.MovieNotFoundException;
import com.example.backendchallenge.movie.dto.MovieDTO;
import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

class MovieServiceTest {

    @Mock
    private MovieRepository repository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateMovie() {
        Movie movie = new Movie(UUID.randomUUID(),"Movie 1", LocalDate.of(2023, 1, 15),
                1, new BigDecimal("50.00"));

        Assertions.assertTrue(movieService.validateMovie(movie));
    }

    @Test
    public void testValidateMovieWithInvalidMovie() {
        Movie movie = new Movie(null, null, null, -6, null);
        Assertions.assertThrows(ValidationException.class, () -> movieService.validateMovie(movie));
    }

    @Test
    void testCreateMovieWithValidMovie() {
        MovieDTO validMovieDTO = new MovieDTO("Movie 1", LocalDate.of(2023, 1, 15),
                1, new BigDecimal("50.00"));
        Movie createdMovie = movieService.createMovie(validMovieDTO);
        assertNotNull(createdMovie);
        verify(repository, times(1)).save(createdMovie);
    }

    @Test
    public void testCreateMovieInvalidMovie() {
        MovieDTO invalidMovieDTO = new MovieDTO(null, null, -6, null);

        Assertions.assertThrows(ValidationException.class, () -> movieService.createMovie(invalidMovieDTO));

        verify(repository, never()).save(any(Movie.class));
    }

    @Test
    public void testGetMovieByIdValidId() {
        Movie mockMovie = new Movie();
        UUID validId = UUID.randomUUID();

        when(repository.findById(validId)).thenReturn(Optional.of(mockMovie));

        Movie result = movieService.getMovieById(validId);

        assertEquals(mockMovie, result);
    }

    @Test
    public void testGetMovieByIdInvalidId() {
        UUID invalidId = UUID.randomUUID();

        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(invalidId));
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> mockMovies = new ArrayList<>();
        mockMovies.add(new Movie(UUID.randomUUID(), "Movie 1", LocalDate.of(2023, 1, 15),
                1, new BigDecimal("50.00")));
        mockMovies.add(new Movie(UUID.randomUUID(), "Movie 2", LocalDate.of(2023, 2, 20),
                3, new BigDecimal("500000.00")));
        mockMovies.add(new Movie(UUID.randomUUID(), "Movie 3", LocalDate.of(2023, 3, 25),
                10, new BigDecimal("1000.00")));

        when(repository.findAll()).thenReturn(mockMovies);

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(mockMovies.size(), result.size());
    }

    @Test
    public void testUpdateMovieValidMovie() {
        UUID movieId = UUID.randomUUID();

        MovieDTO validMovieDTO = new MovieDTO("Updated Movie", LocalDate.of(2023, 1, 15), 1, new BigDecimal("60.00"));
        Movie existingMovie = new Movie(movieId, "Original Movie", LocalDate.of(2022, 1, 15), 2, new BigDecimal("50.00"));

        when(repository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        Movie updatedMovie = movieService.updateMovie(movieId, validMovieDTO);

        verify(repository, times(1)).save(updatedMovie);
    }

    @Test
    public void testUpdateMovieMovieNotFound() {
        UUID nonExistentMovieId = UUID.randomUUID();

        MovieDTO validMovieDTO = new MovieDTO("Updated Movie", LocalDate.of(2023, 1, 15), 1, new BigDecimal("60.00"));

        when(repository.findById(nonExistentMovieId)).thenReturn(Optional.empty());

        Assertions.assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(nonExistentMovieId, validMovieDTO));

        verify(repository, never()).save(any(Movie.class));
    }

    @Test
    public void testDeleteByIdExistingMovie() {
        UUID movieId = UUID.randomUUID();
        Movie existingMovie = new Movie();
        when(repository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        movieService.deleteById(movieId);

        verify(repository, times(1)).deleteById(movieId);
    }

    @Test
    public void testDeleteByIdNonExistingMovie() {
        UUID movieId = UUID.randomUUID();
        when(repository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.deleteById(movieId));    }

    @Test
    public void testGetFilteredMovies() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        List<Movie> expectedMovies = new ArrayList<>();
        expectedMovies.add(new Movie(UUID.randomUUID(), "Movie 1", LocalDate.of(2023, 1, 15),
                7, new BigDecimal("50.00")));
        expectedMovies.add(new Movie(UUID.randomUUID(), "Movie 2", LocalDate.of(2023, 2, 20),
                8, new BigDecimal("75.50")));

        when(repository.getFilteredMovies(startDate, endDate)).thenReturn(expectedMovies);

        List<Movie> filteredMovies = movieService.getFilteredMovies(startDate, endDate);

        assertEquals(expectedMovies.size(), filteredMovies.size());
    }
}