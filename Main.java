package com.example.movie_recommendation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private TextField movieField;
    private Button recommendButton;
    private ListView<String> recommendMoviesField;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        // Load movies from the CSV file
        loadMoviesFromFile("movies.csv");

        // UI Components
        movieField = new TextField();
        movieField.setPromptText("Enter a genre (e.g., Action)");

        recommendButton = new Button("Get Recommendations");
        recommendButton.setOnAction(event -> handleRecommendButton());

        recommendMoviesField = new ListView<>();

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Movie Recommendation App"),
                movieField,
                recommendButton,
                recommendMoviesField
        );

        Scene scene = new Scene(layout, 400, 300);

        // Stage
        primaryStage.setTitle("Movie Recommendation");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        try (BufferedReader reader = new BufferedReader(
                new FileReader(getClass().getResource("/movies.csv").getPath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0].trim();
                    String genre = parts[1].trim();
                    String year = parts[2].trim();
                    movieList.add(new Movie(title, genre, year));
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

        public String getTitle() {
            return title;
        }

        public String getGenre() {
            return genre;
        }

        public String getYear() {
            return year;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
