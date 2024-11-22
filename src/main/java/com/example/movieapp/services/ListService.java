package com.example.movieapp.services;

import com.example.movieapp.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class ListService {
    private final List<Movie> likedMovies = new ArrayList<>();
    private final List<Movie> watchedMovies = new ArrayList<>();
    private final List<Movie> toWatchMovies = new ArrayList<>();

    public void addToLiked(Movie movie) {
        likedMovies.add(movie);
    }

    public void addToWatched(Movie movie) {
        watchedMovies.add(movie);
    }

    public void addToToWatch(Movie movie) {
        toWatchMovies.add(movie);
    }

    public List<Movie> getLikedMovies() {
        return likedMovies;
    }
}
