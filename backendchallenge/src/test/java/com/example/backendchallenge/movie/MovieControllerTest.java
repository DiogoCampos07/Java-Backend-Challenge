package com.example.backendchallenge.movie;

import com.example.backendchallenge.movie.dto.MovieDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
class MovieControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new MovieController(movieService)).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void testCreateMovie() throws Exception {
        UUID movieId = UUID.randomUUID();

        MovieDTO movieDTO = new MovieDTO("Movie 1", LocalDate.of(2023, 1, 15), 1, new BigDecimal("50.00"));

        Movie expectedMovie = new Movie();
        expectedMovie.setId(movieId);
        expectedMovie.setTitle("Movie 1");
        expectedMovie.setLaunchDate(LocalDate.of(2023, 1, 15));
        expectedMovie.setRank(1);
        expectedMovie.setRevenue(new BigDecimal("50.00"));

        when(movieService.createMovie(any(MovieDTO.class))).thenReturn(expectedMovie);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieDTO)));

        // Asserting "launchDate" using assertEquals because the date format might not match
        String jsonResponse = result.andReturn().getResponse().getContentAsString();
        Movie movieResponse = objectMapper.readValue(jsonResponse, Movie.class);
        assertEquals(LocalDate.of(2023, 1, 15), movieResponse.getLaunchDate());

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(movieId.toString()))
                .andExpect(jsonPath("$.title").value("Movie 1"))
                .andExpect(jsonPath("$.rank").value(1))
                .andExpect(jsonPath("$.revenue").value(50.00));
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> moviesList = new ArrayList<>();
        moviesList.add(new Movie(UUID.randomUUID(),"Movie 1", LocalDate.of(2023, 1, 15),
                1, new BigDecimal("50.00")));
        moviesList.add(new Movie(UUID.randomUUID(),"Movie 1", LocalDate.of(2023, 1, 15),
                1, new BigDecimal("50.00")));

        when(movieService.getAllMovies()).thenReturn(moviesList);

        ResponseEntity<List<Movie>> responseEntity = movieController.getAllMovies();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Movie> returnedMovies = responseEntity.getBody();
        assertEquals(moviesList.size(), returnedMovies.size());

    }

    @Test
    public void testGetMovieById() {
        UUID movieId = UUID.randomUUID();

        Movie movie = new Movie(movieId, "Movie 1", LocalDate.of(2023, 1, 15),
                1, new BigDecimal("50.00"));

        when(movieService.getMovieById(movieId)).thenReturn(movie);

        ResponseEntity<Movie> responseEntity = movieController.getMovieById(movieId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Movie returnedMovie = responseEntity.getBody();
        assertEquals(movie.getId(), returnedMovie.getId());
        assertEquals(movie.getTitle(), returnedMovie.getTitle());
    }


    @Test
    public void testUpdateMovie() {
        UUID movieId = UUID.randomUUID();
        MovieDTO movieDTO = new MovieDTO("Movie DTO", LocalDate.of(2023, 4, 16),
                5, new BigDecimal("110.00"));

        Movie updatedMovie = new Movie(movieId, "Movie 1", LocalDate.of(2023, 1, 15),
                1, new BigDecimal("50.00"));

        when(movieService.updateMovie(eq(movieId), any(MovieDTO.class))).thenReturn(updatedMovie);

        ResponseEntity<Movie> response = movieController.updateMovie(movieId, movieDTO);

        verify(movieService, times(1)).updateMovie(eq(movieId), eq(movieDTO));

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getId().equals(movieId);
        assert response.getBody().getTitle().equals("Movie 1");
        assert response.getBody().getLaunchDate().equals(LocalDate.of(2023, 1, 15));
        assert response.getBody().getRank() == 1;
        assert response.getBody().getRevenue().equals(new BigDecimal("50.00"));
    }

    @Test
    public void testDeleteMovieById() {
        UUID movieId = UUID.randomUUID();

        doNothing().when(movieService).deleteById(movieId);

        ResponseEntity<Void> responseEntity = movieController.deleteMovieById(movieId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
    @Test
    public void testGetFilteredMovies() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        List<Movie> filteredMovies = new ArrayList<>();
        filteredMovies.add(new Movie(UUID.randomUUID(), "Movie 1", LocalDate.of(2023, 1, 15),
                1, new BigDecimal("50.00")));
        filteredMovies.add(new Movie(UUID.randomUUID(), "Movie 2", LocalDate.of(2023, 4, 30),
                1, new BigDecimal("50.00")));

        when(movieService.getFilteredMovies(startDate, endDate)).thenReturn(filteredMovies);

        ResponseEntity<List<Movie>> responseEntity = movieController.getFilteredMovies(startDate, endDate);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Movie> returnedMovies = responseEntity.getBody();
        assertEquals(filteredMovies.size(), returnedMovies.size());
    }
}