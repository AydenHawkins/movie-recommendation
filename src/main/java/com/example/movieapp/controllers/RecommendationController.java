package com.example.movieapp.controllers;

import com.example.movieapp.SceneManager;
import com.example.movieapp.services.MovieService;
import com.example.movieapp.models.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.List;


public class RecommendationController {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public ComboBox watchListComboBox;
    @FXML
    public Button discoverSceneButton;
    @FXML
    public Button searchSceneButton;
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
        updateResults(SceneManager.getCurrentMovie());
        watchListComboBox.getItems().addAll("Liked Movies", "To Watch", "Seen");
        watchListComboBox.setValue("Watch Lists");
        watchListComboBox.setStyle("-fx-text-fill: white; -fx-background-color: #424242; -fx-border-color: #474545;");
        watchListComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> handleWatchListSelectionChange(newValue.toString()));
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
                case "Liked Movies":
                    SceneManager.setListTable("Liked_Movies");
                    SceneManager.switchScene(LIST_SCENE_PATH);
                    break;
                case "Seen":
                    SceneManager.setListTable("Watched_Movies");
                    SceneManager.switchScene(LIST_SCENE_PATH);
                    break;
                case "To Watch":
                    SceneManager.setListTable("To_Watch");
                    SceneManager.switchScene(LIST_SCENE_PATH);
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateResults(int query) {
        // Clear previous results
        resultsGrid.getChildren().clear();

        // Get movie data
        List<Movie> movies = movieService.getRecommendedMovies(query);

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
            movieTitle.setWrappingWidth(150);
            movieTitle.setTextAlignment(TextAlignment.CENTER);
            movieTitle.setStyle("-fx-font-weight: bold;");
            movieTitle.setFill(Color.WHITE);

            // Wrap the title inside a fixed container
            VBox titleContainer = new VBox(movieTitle);
            titleContainer.setPrefHeight(50);
            titleContainer.setAlignment(Pos.CENTER);

            // Create label for the movie release date
            Text releaseDate = new Text(movie.getFormattedReleaseDate());
            releaseDate.setTextAlignment(TextAlignment.CENTER);
            releaseDate.setFill(Color.WHITE);

            // Wrap the release date in a fixed container
            VBox dateContainer = new VBox(releaseDate);
            dateContainer.setPrefHeight(30);
            dateContainer.setAlignment(Pos.CENTER);

            // Create a VBox to group poster, title, and release date together
            VBox movieBox = new VBox();
            movieBox.setPrefWidth(275);
            movieBox.setPrefHeight(450);
            movieBox.setAlignment(Pos.TOP_CENTER);
            movieBox.setSpacing(10);
            movieBox.setStyle("-fx-background-color: #424242; -fx-padding: 10; -fx-border-radius: 5; -fx-border-color: #474545; -fx-border-width: 1;");

            // Add components to movieBox
            movieBox.getChildren().addAll(imageView, titleContainer, dateContainer);

            // Initiate popup when a movie is clicked.
            movieBox.setOnMouseClicked(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/movieapp/popup.fxml"));
                    Parent root = loader.load();
                    PopupController popupController = loader.getController();

                    Scene popupScene = new Scene(root);
                    Stage popupStage = new Stage();
                    popupController.getMovieDetails(movie.getId(), popupStage);
                    popupStage.initModality(Modality.APPLICATION_MODAL);
                    popupStage.setTitle(movie.getTitle());
                    popupStage.setScene(popupScene);
                    popupStage.setResizable(false);
                    popupStage.setMaximized(false);
                    popupStage.setIconified(false);
                    popupStage.show();
                    popupController.setProviderLogos(movie.getId());

                    popupStage.show();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });

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

    // Called when the next page button is clicked
    @FXML
    private void handleNextPage() {
        currentPage++;
        updateResults(SceneManager.getCurrentMovie());
    }

    // Called when the previous page button is clicked
    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            updateResults(SceneManager.getCurrentMovie());
        }
    }

}