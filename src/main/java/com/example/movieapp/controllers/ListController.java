package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import com.example.movieapp.services.MovieService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import com.example.movieapp.database.Database;

public class ListController {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    private GridPane resultsGrid;
    @FXML
    private Button likedMovies;
    @FXML
    private Button watchedMovies;
    @FXML
    private Button watchList;

    private final MovieService movieService = new MovieService();

    private void updateResults(String database) {
        // Clear previous results
        resultsGrid.getChildren().clear();

        ArrayList<Integer> movieIDs = new ArrayList<>();
        // Get the movie data
        switch (database) {
            case "Liked_Movies" -> movieIDs = Database.getLikedMovies();
            case "Watched_Movies" -> movieIDs = Database.getWatchedMovies();
            case "To_Watch" -> movieIDs = Database.getToWatch();
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

    private void showMovieDetailsPopup(Movie movie) {
        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(movie.getTitle());

        // Create a VBox for the movie details
        VBox detailsPane = new VBox();
        detailsPane.setAlignment(Pos.CENTER);
        detailsPane.setSpacing(20);
        detailsPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 20;");

        // Add the movie's poster
        ImageView posterView = new ImageView(new Image("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()));
        posterView.setFitWidth(300);
        posterView.setFitHeight(450);

        // Add the movie's title
        Text title = new Text(movie.getTitle());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Add the movie's release date
        Text releaseDate = new Text(movie.getFormattedReleaseDate());

        // Add the movie's overview
        Text overview = new Text(movie.getOverview());
        overview.setWrappingWidth(400);
        overview.setStyle("-fx-font-size: 16px;");

        // Add a close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> popupStage.close());

        // Add all components to the detailsPane
        detailsPane.getChildren().addAll(posterView, title, releaseDate, overview, closeButton);

        // Create a new scene for the popup
        Scene popupScene = new Scene(detailsPane, 500, 700);

        // Set the scene and show the popup
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    @FXML
    private void handleLikedMovies(){
        updateResults("Liked_Movies");
    }

    @FXML
    private void handleWatchedMovies(){
        updateResults("Watched_Movies");
    }

    @FXML
    private void handleWatchList(){
        updateResults("Watch_List");
    }
}
