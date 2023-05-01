package com.softelevation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softelevation.model.Movie;
import com.softelevation.repository.MovieRepository;
import com.softelevation.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

//@WebMvcTest is used to test SpringMvc Components(@Controller,@ControllerAdvice beans but not @Component, @Service or @Repository beans)
@WebMvcTest
public class MovieControllerTests {

    //@MockBean is used when our test case depends on Spring IOC container or Application Context and if the required bean is already defined/available then we use @MockBean at top of it
    @MockBean
    private MovieService movieService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MovieRepository movieRepository;
    private Movie avatarMovie;
    private Movie titanicMovie;

    @BeforeEach
    public void init() {
        avatarMovie = new Movie();
        avatarMovie.setId(1L);
        avatarMovie.setName("Avatar");
        avatarMovie.setGenera("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        titanicMovie = new Movie();
        titanicMovie.setId(2L);
        titanicMovie.setName("Titanic");
        titanicMovie.setGenera("Romance");
        titanicMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 20));
    }

    @Test
    public void shouldCreateNewMovie() throws Exception {

        when(movieService.saveMovie(any(Movie.class))).thenReturn(avatarMovie);
        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avatarMovie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(avatarMovie.getName())))
                .andExpect(jsonPath("$.genera", is(avatarMovie.getGenera())))
                .andExpect(jsonPath("$.releaseDate", is(avatarMovie.getReleaseDate().toString())));
    }

    @Test
    public void shouldFetchAllMovies() throws Exception {
        List<Movie> list = new ArrayList<>();
        list.add(avatarMovie);
        list.add(titanicMovie);
        when(movieService.getAllMovies()).thenReturn(list);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    public void shouldFetchOneMovieById() throws Exception {

        when(movieService.getMovieByMovieId(anyLong())).thenReturn(avatarMovie);
        mockMvc.perform(get("/movies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(avatarMovie.getName())))
                .andExpect(jsonPath("$.genera", is(avatarMovie.getGenera())));
    }

    @Test
    public void shouldUpdateMovie() throws Exception {
        when(movieService.updateMovie(any(Movie.class), anyLong())).thenReturn(avatarMovie);
        mockMvc.perform(put("/movies/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avatarMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(avatarMovie.getName())))
                .andExpect(jsonPath("$.genera", is(avatarMovie.getGenera())));
    }

    @Test
    public void shouldDeleteMovie() throws Exception {

        doNothing().when(movieService).deleteMovieByMovieId(anyLong());
        mockMvc.perform(delete("/movies/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}