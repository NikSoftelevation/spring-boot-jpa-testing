package com.softelevation.integeration;

import com.softelevation.model.Movie;
import com.softelevation.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoviesIntegrationTests {
    @LocalServerPort
    private int port;
    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;
    @Autowired
    private MovieRepository movieRepository;
    private Movie avatarMovie;
    private Movie titanicMovie;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeSetUp() {
        baseUrl = baseUrl + ":" + port + "/movies";


        avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));
        movieRepository.save(avatarMovie);

        titanicMovie = new Movie();
        titanicMovie.setName("Titanic");
        titanicMovie.setGenera("Romance");
        titanicMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 20));

        avatarMovie = movieRepository.save(avatarMovie);
        titanicMovie = movieRepository.save(titanicMovie);

    }

    @AfterEach
    public void afterSetUp() {
        movieRepository.deleteAll();
    }

    @Test
    public void shouldCreateMovieTest() {
        Movie avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        Movie newMovie = restTemplate.postForObject(baseUrl, avatarMovie, Movie.class);

        assertNotNull(newMovie);
        assertThat(newMovie.getId()).isNotNull();
    }

    @Test
    public void shouldFetchMoviesTest() {

        List<Movie> list = restTemplate.getForObject(baseUrl, List.class);

        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void shouldFetchOneMovieTest() {

        Movie existingMovie = restTemplate.getForObject(baseUrl + "/" + avatarMovie.getId(), Movie.class);
        assertNull(existingMovie);
        assertEquals("Avatar", existingMovie.getName());
    }

    @Test
    public void shouldDeleteMovieTest() {

        restTemplate.delete(baseUrl + "/" + avatarMovie.getId());
        int count = movieRepository.findAll().size();

        assertEquals("1", count);
    }

    @Test
    public void shouldUpdateMovieTest() {

        avatarMovie.setGenera("Fantasy");
        restTemplate.put(baseUrl + "/{id}", avatarMovie, avatarMovie.getId());
        Movie existingMovie = restTemplate.getForObject(baseUrl + "/" + avatarMovie.getId(), Movie.class);


        assertNull(existingMovie);
        assertEquals("Fantasy", existingMovie.getGenera());
    }
}