package com.softelevation;

import com.softelevation.model.Movie;
import com.softelevation.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    private Movie avatarMovie;

    private Movie titanicMovie;

    @BeforeEach
    public void init() {
        avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        titanicMovie = new Movie();
        titanicMovie.setName("Titanic");
        titanicMovie.setGenera("Romance");
        titanicMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 20));

    }

    @Test
    public void save() {

        //Arrange->Setting up data that is required for the test case

        //Act->Calling a method/unit that is being tested
        Movie savedMovie = movieRepository.save(avatarMovie);

        //Assert->Verifying the expected result is correct or not
        assertNotNull(savedMovie);
        assertThat(savedMovie.getId()).isNotEqualTo(null);
    }

    @Test
    public void getAllMovies() {

        movieRepository.save(avatarMovie);

        movieRepository.save(titanicMovie);

        List<Movie> list = movieRepository.findAll();

        assertNotNull(list);
        assertThat(list).isNotNull();
        assertEquals(2, list.size());
    }

    @Test
    public void getMovieByMovieId() {

        Movie existingMovie = movieRepository.findById(avatarMovie.getId()).get();

        assertNotNull(existingMovie);
        assertEquals("Action", existingMovie.getGenera());
        assertThat(avatarMovie.getReleaseDate()).isBefore(LocalDate.of(2000, Month.APRIL, 23));
    }

    @Test
    public void updateMovie() {
        movieRepository.save(avatarMovie);
        Movie existingmovie = movieRepository.findById(avatarMovie.getId()).get();

        existingmovie.setGenera("Fantasy");
        Movie newMovie = movieRepository.save(existingmovie);

        assertEquals("Fantasy", newMovie.getGenera());
        assertEquals("Avatar", newMovie.getName());
    }

    @Test
    public void deleteMovie() {
        movieRepository.save(avatarMovie);
        Long id = avatarMovie.getId();

        movieRepository.save(titanicMovie);

        movieRepository.delete(avatarMovie);

        Optional<Movie> existingMovie = movieRepository.findById(id);

        List<Movie> list = movieRepository.findAll();
        assertEquals(1, list.size());
        assertThat(existingMovie).isEmpty();
    }

    @Test
    public void geyMoviesByGenera() {
        movieRepository.save(avatarMovie);
        movieRepository.save(titanicMovie);

        List<Movie> list = movieRepository.findByGenera("Romance");

        assertNotNull(list);
        assertThat(list.size()).isEqualTo(1);
    }
}