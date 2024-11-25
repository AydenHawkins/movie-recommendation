package com.example.movieapp.services;

import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.MovieResponse;
import com.example.movieapp.models.Movie;
import com.google.gson.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.util.Map;
import java.util.stream.Collectors;

/**Class with methods to search for movies with the TMDB API with different parameters and requirements
 *
 */
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

    /**Takes a movie ID and returns a Movie object from the result of the API call
     *
     * @param movieID the ID of the movie to search for
     * @return The movie object of the corrosponding ID
     */
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

    public String getCertificationByMovieId(int movieId) {
        String endpoint = "movie/" + movieId + "/release_dates";
        String queryParameters = ""; // No additional query parameters
        int page = 1; // Pagination isn't required here, but a default value is necessary

        String jsonResponse = apiClient.sendRequestByID(endpoint, queryParameters, page);

        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            JsonArray results = jsonObject.getAsJsonArray("results");

            for (JsonElement result : results) {
                JsonObject countryData = result.getAsJsonObject();
                // Check for US certification (or your preferred country code)
                if (countryData.get("iso_3166_1").getAsString().equals("US")) {
                    JsonArray releaseDates = countryData.getAsJsonArray("release_dates");
                    for (JsonElement releaseDate : releaseDates) {
                        JsonObject releaseInfo = releaseDate.getAsJsonObject();
                        if (releaseInfo.has("certification")) {
                            String certification = releaseInfo.get("certification").getAsString();
                            if (!certification.isEmpty()) {
                                return certification;
                            }
                        }
                    }
                }
            }
        }
        return "N/A"; // Return "N/A" if no certification is found
    }

    public List<String> getProviderLogos(int movieId) {
        String endpoint = "movie/" + movieId + "/watch/providers";
        String queryParameters = "";
        int page = 1;

        String jsonResponse = apiClient.sendRequestByID(endpoint, queryParameters, page);

        List<String> logoUrls = new ArrayList<>();

        if (jsonResponse != null) {
            try {
                // Debugging: Print the raw response to check its structure
                System.out.println("API Response: " + jsonResponse);

                JsonObject rootObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                if (rootObject.has("results")) {
                    JsonObject results = rootObject.getAsJsonObject("results");

                    // Replace "US" with your target country code
                    String targetCountry = "US"; // Change to match your desired country
                    if (results.has(targetCountry)) {
                        JsonObject countryObject = results.getAsJsonObject(targetCountry);

                        if (countryObject != null && countryObject.has("flatrate")) {
                            JsonArray streamingProviders = countryObject.getAsJsonArray("flatrate");

                            // Add the first 3 providers' logos
                            for (int i = 0; i < Math.min(3, streamingProviders.size()); i++) {
                                JsonObject provider = streamingProviders.get(i).getAsJsonObject();
                                if (provider.has("logo_path")) {
                                    String logoPath = provider.get("logo_path").getAsString();
                                    String logoUrl = "https://image.tmdb.org/t/p/w500" + logoPath;
                                    logoUrls.add(logoUrl);
                                } else {
                                    System.out.println("Provider missing 'logo_path' field.");
                                }
                            }
                        } else {
                            System.out.println("No 'flatrate' key found for country: " + targetCountry);
                        }
                    } else {
                        System.out.println("Country code '" + targetCountry + "' not found in 'results'.");
                    }
                } else {
                    System.out.println("'results' key not found in API response.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("API response is null.");
        }

        // Fill remaining slots with null if fewer than 3 logos exist
        while (logoUrls.size() < 3) {
            logoUrls.add(null);
        }

        return logoUrls;
    }


}
