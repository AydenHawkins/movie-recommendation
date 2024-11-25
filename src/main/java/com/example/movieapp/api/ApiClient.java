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
            e.printStackTrace();
            return null;
        }
    }

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
            e.printStackTrace();
            return null;
        }
    }

}
