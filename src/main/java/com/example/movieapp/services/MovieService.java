package com.example.movieapp.services;

import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.MovieResponse;
import com.example.movieapp.models.Movie;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieService {
    private static final ApiClient apiClient = new ApiClient();
    private static final Gson gson = new Gson();
    private static final Map<String, String> GENRE_MAP = Map.of(
            "Action", "28",
            "Comedy", "35",
            "Drama", "18",
            "Horror", "27",
            "Romance", "10749",
            "Science Fiction", "878"
    );

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
        String response = apiClient.sendRequest("movie/popular", encodedQuery, page);

        // Deserialize response.
        MovieResponse movieResponse = gson.fromJson(response, MovieResponse.class);

        // Return the list of movies (or an empty list if no movies found)
        return movieResponse != null ? movieResponse.getResults() : new ArrayList<>();
    }

    public static Movie getMovieByID(String movieID) {
        String encodedQuery = URLEncoder.encode(movieID, StandardCharsets.UTF_8);

        // Send request.
        String response = apiClient.sendRequestByID("movie/" + encodedQuery, "", 1);

        // Return the movie object
        return gson.fromJson(response, Movie.class);
    }


    public List<Movie> discoverMovies(Map<String, String> filters, int page) {
        String queryParams = filters.entrySet().stream()
                .map(entry -> "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String response = apiClient.sendRequest("discover/movie", queryParams, page);

        MovieResponse movieResponse = gson.fromJson(response, MovieResponse.class);

        return movieResponse != null ? movieResponse.getResults() : new ArrayList<>();
    }

    public String getGenreId(String genreName) {
        return GENRE_MAP.getOrDefault(genreName, "");
    }

}
