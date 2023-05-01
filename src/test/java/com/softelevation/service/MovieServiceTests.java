package com.softelevation.service;

import com.softelevation.model.Movie;
import com.softelevation.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTests {
    @InjectMocks
    private MovieService movieService;
    @Mock
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
    public void save() {

        when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
        Movie newMovie = movieService.saveMovie(avatarMovie);
        assertNotNull(newMovie);
        assertThat(newMovie.getName()).isEqualTo("Avatar");
    }

    @Test
    public void getMovies() {

        List<Movie> list = new ArrayList<>();
        list.add(titanicMovie);
        list.add(avatarMovie);
        when(movieRepository.findAll()).thenReturn(list);

        List<Movie> movies = movieService.getAllMovies();

        assertEquals(2, movies.size());
        assertNotNull(movies);
    }

    @Test
    public void getMovieById() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));

        Movie existngMovie = movieService.getMovieByMovieId(1L);
        assertNotNull(existngMovie);
        assertThat(existngMovie.getId()).isNotEqualTo(1L);
    }

    @Test
    public void getMovieByIdForException() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(avatarMovie));

        assertThrows(RuntimeException.class, () ->
                movieService.getMovieByMovieId(2L));
    }

    @Test
    public void updateMovie() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);

        avatarMovie.setGenera("Fantasy");
        Movie updatedMovie = movieService.updateMovie(avatarMovie, 1L);

        assertNotNull(updatedMovie);
        assertEquals("Fantasy", updatedMovie.getGenera());
    }

    @Test
    public void deleteMovie() {

        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
        doNothing().when(movieRepository).delete(any(Movie.class));

        movieService.deleteMovieByMovieId(1L);

        verify(movieRepository, times(1)).delete(avatarMovie);
    }
}