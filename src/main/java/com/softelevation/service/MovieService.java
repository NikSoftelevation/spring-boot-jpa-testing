package com.softelevation.service;

import com.softelevation.model.Movie;

import java.util.List;

public interface MovieService {

    public Movie saveMovie(Movie movie);

    public Movie updateMovie(Movie movie, Long movieId);

    public List<Movie> getAllMovies();

    public Movie getMovieByMovieId(Long movieId);

    public void deleteMovieByMovieId(Long movieId);
}
