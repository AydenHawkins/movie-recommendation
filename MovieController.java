package com.example.movie_recommendation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieController {

    @FXML
    private TextField inputField;

    @FXML
    private Button recommendButton;

    @FXML
    private ListView<String> movieListView;

    private List<Movie> movieList = new ArrayList<>();

    @FXML
    public void initialize() {
        loadMoviesFromFile("movies.csv");
        recommendButton.setOnAction(event -> handleRecommendButton());
    }

    private void handleRecommendButton() {
        String keyword = inputField.getText().toLowerCase();
        List<String> recommendations = new ArrayList<>();
        for (Movie movie : movieList) {
            if (movie.getGenre().toLowerCase().contains(keyword)) {
                recommendations.add(movie.getTitle());
            }
        }
        movieListView.getItems().clear();
        movieListView.getItems().addAll(recommendations.isEmpty() ?
                List.of("No recommendations found") : recommendations);
    }

    private void loadMoviesFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String title = parts[0].trim();
                    String genre = parts[1].trim();
                    movieList.add(new Movie(title, genre));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading movies: " + e.getMessage());
        }
    }
        public static class Movie {
            private final String title;
            private final String genre;

            public Movie(String title, String genre) {
                this.title = title;
                this.genre = genre;
            }

            public String getTitle() {
                return title;
            }

            public String getGenre() {
                return genre;
            }

        }
    }
}