package com.softelevation.service.impl;

import com.softelevation.exception.ResourceNotFoundException;
import com.softelevation.model.Movie;
import com.softelevation.repository.MovieRepository;
import com.softelevation.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(Movie movie, Long movieId) {

        Movie movieToUpdate = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("movie with movieId " + movieId + "not found"));
        movieToUpdate.setName(movie.getName());
        movieToUpdate.setReleaseDate(movie.getReleaseDate());
        movieToUpdate.setGenera(movieToUpdate.getGenera());

        return movieRepository.save(movieToUpdate);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovieByMovieId(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie with movieId " + movieId + "not found"));
    }

    @Override
    public void deleteMovieByMovieId(Long movieId) {

        Movie deleteMovieByMovieId = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNotFoundException("Movie with movieId" + movieId + "not found"));
        movieRepository.delete(deleteMovieByMovieId);
    }
}
