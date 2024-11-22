package com.example.movieapp.services;

import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.MovieResponse;
import com.example.movieapp.models.Movie;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.net.URLEncoder;

public class MovieService {
    private final ApiClient apiClient = new ApiClient();
    private final Gson gson = new Gson();

    // Searches for a movie based on a given string.
    public List<Movie> searchMovies(String query, int page) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String queryParameters = "&query=" + encodedQuery;

        // Send request.
        String response = apiClient.sendRequest("search/movie", queryParameters, page);

        // Deserialize response.
        MovieResponse movieResponse = gson.fromJson(response, MovieResponse.class);

        // Return the list of movies (or an empty list if no movies found)
        return movieResponse != null ? movieResponse.getResults() : new ArrayList<>();
    }

    // Gets list of popular movies to use as default discover list of movies.
    public List<Movie> getPopularMovies(String query, int page) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        // Send request.
        String response = apiClient.sendRequest("/movie", encodedQuery, page);

        // Deserialize response.
        MovieResponse movieResponse = gson.fromJson(response, MovieResponse.class);

        // Return the list of movies (or an empty list if no movies found)
        return movieResponse != null ? movieResponse.getResults() : new ArrayList<>();
    }
}
