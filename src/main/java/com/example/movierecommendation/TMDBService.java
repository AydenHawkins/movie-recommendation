package com.example.movierecommendation;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TMDBService {
    private static final String API_KEY = "2944647b92af0a01d0547271ecdf9c59";
    private static final String API_URL = "https://api.themoviedb.org/3";
    private final HttpClient client;
    private final Gson gson;

    public TMDBService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    private String buildURL(String endpoint, String parameters) {
        return API_URL + endpoint + "?api_key=" + API_KEY + (parameters != null ? "&" + parameters : "");
    }

    public String fetch(String endpoint, String parameters) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildURL(endpoint, parameters)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch data: " + response.body());
        }
        return response.body();
    }

    // Gson accessor
    public Gson getGson() {
        return gson;
    }
}
