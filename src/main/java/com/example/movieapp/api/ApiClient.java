package com.example.movieapp.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**Handles all interactions with the TMDB API
 *
 */
public class ApiClient {
    private static final String API_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "2944647b92af0a01d0547271ecdf9c59";

    /**
     * Sends HTTP request to API.
     * @param endpoint Endpoint of API URL where response is sent.
     * @param queryParameters Parameters appended to request URL.
     * @param page The page of results returned from the API request.
     * @return Returns a JSON response to be parsed.
     */
    public String sendRequest(String endpoint, String queryParameters, int page) {
        try {
            String url = String.format("%s/%s?api_key=%s%s&page=%d", API_URL, endpoint, API_KEY, queryParameters, page);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Sends HTTP request to API using a movie ID.
     * @param endpoint Endpoint of API URL where response is sent.
     * @param queryParameters Parameters appended to request URL.
     * @param page The page of results returned from the API request.
     * @return Returns a JSON response to be parsed.
     */
    public String sendRequestByID(String endpoint, String queryParameters, int page) {
        try {
            String url = String.format("%s/%s%s?api_key=%s&page=%d", API_URL, endpoint, queryParameters, API_KEY, page);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
