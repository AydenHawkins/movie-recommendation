package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.services.MovieService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.List;

public class ListController {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    private GridPane resultsGrid;
    @FXML
    private CheckBox likedMovies;
    @FXML
    private CheckBox watchedMovies;
    @FXML
    private CheckBox watchList;

    private final MovieService movieService = new MovieService();

    private void updateResults(String database) {
        // Clear previous results
        resultsGrid.getChildren().clear();

        ArrayList<Integer> movieIDs = new ArrayList<Integer>();
        // Get the movie data
        switch (database) {
            case "Liked_Movies" -> {
                movieIDs = database.getLikedMovies();
            }
            case "Watched_Movies" -> {
                movieIDs = database.getWatchedMovies();
            }
            case "To_Watch" -> {
                movieIDs = database.getToWatch();
            }
        }
        //must add api calls to create a list of movie objects
        List<Movie> movies = new ArrayList<>();
        for (int movieID : movieIDs){
            movies.add(MovieService.getMovieByID(String.valueOf(movieID), 1));
        }

        int row = 0;
        int col = 0;

        // Add movie posters, titles, and release dates to the grid
        for (Movie movie : movies) {
            // Create ImageView for poster
            ImageView imageView = new ImageView(new Image("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()));
            imageView.setFitWidth(275);
            imageView.setFitHeight(450);

            // Create label for the movie title
            Text movieTitle = new Text(movie.getTitle());
            movieTitle.setWrappingWidth(150); // Match VBox width
            movieTitle.setTextAlignment(TextAlignment.CENTER);
            movieTitle.setStyle("-fx-font-weight: bold;");
            movieTitle.setStyle("-fx-text-alignment: center;");

            // Wrap the title inside a fixed container.
            VBox titleContainer = new VBox(movieTitle);
            titleContainer.setPrefHeight(50);
            titleContainer.setAlignment(Pos.CENTER);
            titleContainer.setStyle("-fx-font-weight: bold;");

            // Create label for the movie release date
            Text releaseDate = new Text(movie.getFormattedReleaseDate());
            releaseDate.setTextAlignment(TextAlignment.CENTER);

            // Wrap the release date in a fixed container.
            VBox dateContainer = new VBox(releaseDate);
            dateContainer.setPrefHeight(30);
            dateContainer.setAlignment(Pos.CENTER);

            // Create a VBox to group poster, title, and release date together
            VBox movieBox = new VBox();
            movieBox.setPrefWidth(275);
            movieBox.setPrefHeight(450);
            movieBox.setAlignment(Pos.TOP_CENTER);
            movieBox.setSpacing(10);
            movieBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-radius: 10; -fx-border-color: #ddd; -fx-border-width: 1;");

            // Add components to movieBox
            movieBox.getChildren().addAll(imageView, titleContainer, dateContainer);

            movieBox.setOnMouseClicked(event -> showMovieDetailsPopup(movie));

            // Add the VBox to the grid
            resultsGrid.add(movieBox, col, row);

            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }
    }
}
