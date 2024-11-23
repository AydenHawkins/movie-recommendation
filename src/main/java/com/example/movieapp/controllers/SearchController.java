package com.example.movieapp.controllers;

import com.example.movieapp.SceneManager;
import com.example.movieapp.services.MovieService;
import com.example.movieapp.models.Movie;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.List;

public class SearchController {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Button searchSceneButton;
    @FXML
    public Button discoverSceneButton;
    @FXML
    public ChoiceBox watchListChoiceBox;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private GridPane resultsGrid;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button nextPageButton;

    private final MovieService movieService = new MovieService();
    private static final String SEARCH_SCENE_PATH = "/com/example/movieapp/search.fxml";
    private static final String DISCOVER_SCENE_PATH = "/com/example/movieapp/discover.fxml";
    private static final String LIST_SCENE_PATH = "/com/example/movieapp/list.fxml";
    private int currentPage = 1;

    @FXML
    public void initialize() {
        loadDefaultMovies();
        watchListChoiceBox.getItems().addAll("Liked Movies", "To Watch", "Seen");
        watchListChoiceBox.setValue("Watch Lists");
        watchListChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> handleWatchListSelectionChange(newValue.toString()));
    }

    private void loadDefaultMovies() {
        updateResults("");
    }

    @FXML
    public void handleSearchSceneButton() throws IOException {
        SceneManager.switchScene(SEARCH_SCENE_PATH);
    }

    @FXML
    public void handleDiscoverSceneButton() throws IOException {
        SceneManager.switchScene(DISCOVER_SCENE_PATH);
    }

    public void handleWatchListSelectionChange(String selectedList) {
        try {
            switch (selectedList) {
                case "Liked Movies", "Seen", "To Watch":
                    SceneManager.switchScene(LIST_SCENE_PATH);
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Called when the search button is clicked
    @FXML
    public void handleSearchButton() {
        String query = searchField.getText();
        if (query != null && !query.isEmpty()) {
            currentPage = 1;
            updateResults(query);
        }
    }

    private void updateResults(String query) {
        // Clear previous results
        resultsGrid.getChildren().clear();

        List<Movie> movies;
        if (query.isEmpty()) {
            movies = movieService.getPopularMovies(query, currentPage);
        } else {
            // Get the movie data
            movies = movieService.searchMovies(query, currentPage);
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

        // Disable next/prev buttons based on the page
        prevPageButton.setDisable(currentPage == 1);
        nextPageButton.setDisable(movies.size() < 20);
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

    // Called when the next page button is clicked
    @FXML
    private void handleNextPage() {
        currentPage++;
        updateResults(searchField.getText());
    }

    // Called when the previous page button is clicked
    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            updateResults(searchField.getText());
        }
    }
}
