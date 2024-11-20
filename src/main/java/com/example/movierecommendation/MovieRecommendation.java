package com.example.movierecommendation;

import com.google.gson.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class MovieRecommendation {
    private static final String API_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "2944647b92af0a01d0547271ecdf9c59";

    public static void main(String[] args) {
        String genre = "28";
        getMoviesByGenre(genre);
    }

    public static void getMoviesByGenre(String genre) {
        try {
            List<Movie> movies = new ArrayList<>();
            int page = 1;
            int maxPage = 500;

            while (page <= maxPage) {
                // Build the API URL for the current page
                String url = API_URL + "/discover/movie?api_key=" + API_KEY + "&with_genres=" + genre + "&page=" + page;

                // Create the HTTP client
                HttpClient client = HttpClient.newHttpClient();

                // Create the HTTP request
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();

                // Send the request and get the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Check the response status
                if (response.statusCode() == 200) {
                    String responseBody = response.body();

                    // Parse the JSON response using Gson
                    Gson gson = new Gson();
                    MovieList movieList = gson.fromJson(responseBody, MovieList.class);

                    List<Movie> currentPageMovies = movieList.getMovies();
                    movies.addAll(currentPageMovies);

                    // Check if we have more pages to fetch
                    if (page == maxPage) {
                        break;
                    }
                    page++;
                } else if (response.statusCode() == 400) {
                    System.out.print("Error 400: - Bad request. Reached maximum number of allowed pages.");
                    break;
                } else {
                    System.out.println("Error: " + response.statusCode());
                    break;
                }
            }

            // Writes out all the movies
            BufferedWriter writer = writer(movies);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedWriter writer(List<Movie> movies) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("movies_output.txt"));
        for (Movie movie : movies) {
            writer.write("Title: " + movie.getTitle());
            writer.newLine();
            writer.write("Release Date: " + movie.getReleaseDate());
            writer.newLine();
            writer.write("Rating: " + movie.getRating());
            writer.newLine();
            writer.write("------------");
            writer.newLine();
        }
        return writer;
    }
}
