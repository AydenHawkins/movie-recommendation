package com.example.movieapp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final String API_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "2944647b92af0a01d0547271ecdf9c59";

    public String sendRequest(String endpoint, String query, int page) {
        try {
            String url = String.format("%s/%s?api_key=%s&query=%s&page=%d", API_URL, endpoint, API_KEY, query, page);
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
