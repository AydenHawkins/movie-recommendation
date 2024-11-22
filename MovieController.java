package com.example.movie_recommendation2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieController {

    @FXML
    private TextField movieField;

    @FXML
    private Button recommendButton;

    @FXML
    private ListView<String> recommendMoviesField;

    private List<Movie> movieList = new ArrayList<>();

    @FXML
    public void initialize() {
        loadMoviesFromFile("movies.csv");
        recommendButton.setOnAction(event -> handleRecommendButton());
    }

    private void handleRecommendButton() {
        String keyword = movieField.getText().toLowerCase();
        List<String> recommendations = new ArrayList<>();
        for (Movie movie : movieList) {
            if (movie.getGenre().toLowerCase().contains(keyword)) {
                recommendations.add(movie.getTitle());
            }
        }
        recommendMoviesField.getItems().clear();
        recommendMoviesField.getItems().addAll(recommendations.isEmpty() ?
                List.of("No recommendations found") : recommendations);
    }

    private void loadMoviesFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0].trim();
                    String genre = parts[1].trim();
                    String year = parts[2].trim();
                    movieList.add(new Movie(title, genre,year));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading movies: " + e.getMessage());
        }
    }
    public static class Movie {
        private final String title;
        private final String genre;
        private final String year;

        public Movie(String title, String genre, String year) {
            this.title = title;
            this.genre = genre;
            this.year = year;
        }

        public String getYear() {
            return year;
        }

        public String getTitle() {
            return title;
        }

        public String getGenre() {
            return genre;
        }


    }
}