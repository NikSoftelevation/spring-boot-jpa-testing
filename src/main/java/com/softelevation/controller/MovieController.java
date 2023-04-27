package com.softelevation.controller;

import com.softelevation.model.Movie;
import com.softelevation.repository.MovieRepository;
import com.softelevation.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;

    @PostMapping("")
    public ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.saveMovie(movie), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Movie> getMovieByMovieId(@PathVariable("movieId") Long movieId) {
        return new ResponseEntity<>(movieService.getMovieByMovieId(movieId), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie, @PathVariable("movieId") Long movieId) {
        return new ResponseEntity<>(movieService.updateMovie(movie, movieId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("")
    public void deleteMovieByMovieId(@PathVariable("movieId") Long movieId) {
        movieService.deleteMovieByMovieId(movieId);
    }
}