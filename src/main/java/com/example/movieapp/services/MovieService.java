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

    // Map of genres to their ID's
    private static final Map<String, String> GENRE_MAP = Map.ofEntries(
            Map.entry("Action", "28"),
            Map.entry("Adventure", "12"),
            Map.entry("Animation", "16"),
            Map.entry("Comedy", "35"),
            Map.entry("Crime", "80"),
            Map.entry("Documentary", "99"),
            Map.entry("Drama", "18"),
            Map.entry("Family", "10751"),
            Map.entry("Fantasy", "14"),
            Map.entry("History", "36"),
            Map.entry("Horror", "27"),
            Map.entry("Music", "10402"),
            Map.entry("Mystery", "9648"),
            Map.entry("Romance", "10749"),
            Map.entry("Science Fiction", "878"),
            Map.entry("TV Movie", "10770"),
            Map.entry("Thriller", "53"),
            Map.entry("War", "10752"),
            Map.entry("Western", "37")
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

    /**
     * Sends HTTP request to API discover page.
     * @param filters Filters selected by user.
     * @param page The page of results the API returns.
     * @return Returns a list of movie objects as a result of the API call.
     */
    public List<Movie> discoverMovies(Map<String, String> filters, int page) {
        String queryParams = filters.entrySet().stream()
                .map(entry -> "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String response = apiClient.sendRequest("discover/movie", queryParams, page);

        MovieResponse movieResponse = gson.fromJson(response, MovieResponse.class);

        return movieResponse != null ? movieResponse.getResults() : new ArrayList<>();
    }

    /**
     * Gets the genre ID of a movie from a predefined map of genres.
     * @param genreName The name of the genre.
     * @return Returns the genre ID of a movie.
     */
    public String getGenreId(String genreName) {
        return GENRE_MAP.getOrDefault(genreName, "");
    }

    /**
     * Gets the MPAA rating of a movie.
     * @param movieId The movie's ID.
     * @return Returns the MPAA rating.
     */
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

    /**
     * Gets a list of streaming providers and their logos.
     * @param movieId The movie's ID.
     * @return Returns the list of streaming provider's logos.
     */
    public List<String> getProviderLogos(int movieId) {
        String endpoint = "movie/" + movieId + "/watch/providers";
        String queryParameters = "";
        int page = 1;

        // Send HTTP request.
        String jsonResponse = apiClient.sendRequestByID(endpoint, queryParameters, page);

        List<String> logoUrls = new ArrayList<>();

        // Parse json response.
        if (jsonResponse != null) {
            try {
                JsonObject rootObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                if (rootObject.has("results")) {
                    JsonObject results = rootObject.getAsJsonObject("results");

                    String targetCountry = "US";
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

    /**
     * Gets a list of recommended movies based on a given movie.
     * @param movieId The given movie's ID.
     * @return Returns a list of recommended movies.
     */
    public List<Movie> getRecommendedMovies(int movieId) {
        String endpoint = "movie/" + movieId + "/recommendations";
        String queryParameters = "";
        int page = 1;

        // Send HTTP request
        String jsonResponse = apiClient.sendRequestByID(endpoint, queryParameters, page);

        // Initialize the list to hold recommended movies
        List<Movie> recommendedMovies = new ArrayList<>();

        // Check if the API response is not null
        if (jsonResponse != null) {
            try {
                // Parse the JSON response
                JsonObject rootObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                JsonArray results = rootObject.getAsJsonArray("results");

                // Loop through the results and convert each movie into a Movie object
                for (JsonElement element : results) {
                    JsonObject movieObject = element.getAsJsonObject();
                    Movie movie = new Movie();

                    // Set the movie details
                    movie.setId(movieObject.get("id").getAsInt());
                    movie.setTitle(movieObject.get("title").getAsString());
                    movie.setReleaseDate(movieObject.get("release_date").getAsString());
                    movie.setOverview(movieObject.get("overview").getAsString());
                    movie.setPosterPath("https://image.tmdb.org/t/p/w500" + movieObject.get("poster_path").getAsString());
                    movie.setVoteAverage(movieObject.get("vote_average").getAsFloat());
                    movie.setPopularity(movieObject.get("popularity").getAsFloat());
                    movie.setRuntime(movieObject.has("runtime") && !movieObject.get("runtime").isJsonNull()
                            ? movieObject.get("runtime").getAsInt()
                            : 0);
                    movie.setRevenue(movieObject.has("revenue") && !movieObject.get("revenue").isJsonNull()
                            ? movieObject.get("venue").getAsDouble()
                            : 0);
                    movie.setBudget(movieObject.has("budget") && !movieObject.get("budget").isJsonNull()
                            ? movieObject.get("budget").getAsDouble()
                            : 0);


                    // Add the movie to the recommended movies list
                    recommendedMovies.add(movie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Return the list of recommended movies
        System.out.print(recommendedMovies);
        return recommendedMovies;
    }


}
