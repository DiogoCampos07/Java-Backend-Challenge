package com.example.backendchallenge.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    @Query("select m from movies m where m.launchDate between ?1 and ?2")
    List<Movie> getFilteredMovies(LocalDate startDate, LocalDate endDate);
}
