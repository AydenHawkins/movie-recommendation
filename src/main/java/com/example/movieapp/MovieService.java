package com.example.movieapp;

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

        // Send request.
        String response = apiClient.sendRequest("search/movie", encodedQuery, page);

        // Deserialize response.
        MovieResponse movieResponse = gson.fromJson(response, MovieResponse.class);

        // Return the list of movies (or an empty list if no movies found)
        return movieResponse != null ? movieResponse.getResults() : new ArrayList<>();

    }
}
