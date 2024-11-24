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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverController {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public ChoiceBox watchListChoiceBox;
    @FXML
    public Button discoverSceneButton;
    @FXML
    public Button searchSceneButton;
    @FXML
    public ComboBox genreComboBox;
    @FXML
    public TextField yearTextField;
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
    private Map<String, String> currentFilters = new HashMap<>();


    @FXML
    public void initialize() {
        loadDefaultMovies();
        watchListChoiceBox.getItems().addAll("Liked Movies", "To Watch", "Seen");
        watchListChoiceBox.setValue("Watch Lists");
        watchListChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> handleWatchListSelectionChange(newValue.toString()));
        genreComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Science Fiction");
    }

    private void loadDefaultMovies() {
        updateResults("", null);
    }

    @FXML
    public void handleSearchSceneButton() throws IOException {
        SceneManager.switchScene(SEARCH_SCENE_PATH);
    }

    @FXML
    public void handleDiscoverSceneButton() throws IOException {
        SceneManager.switchScene(DISCOVER_SCENE_PATH);
    }

    @FXML
    public void handleApplyFiltersButton() throws IOException {
        applyFilters();
    }

    public void handleWatchListSelectionChange(String selectedList) {
        try {
            switch (selectedList) {
                case "Liked Movies":
                    SceneManager.setListTable("Liked_Movies");
                    SceneManager.switchScene(LIST_SCENE_PATH);
                case "Seen":
                    SceneManager.setListTable("Watched_Movies");
                    SceneManager.switchScene(LIST_SCENE_PATH);
                case "To Watch":
                    SceneManager.setListTable("To_Watch");
                    SceneManager.switchScene(LIST_SCENE_PATH);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateResults(String query, Map<String, String> filters) {
        // Clear previous results
        resultsGrid.getChildren().clear();

        // Get movie data: Check if filters are applied
        List<Movie> movies = (filters == null || filters.isEmpty()) ?
                movieService.getPopularMovies(query, currentPage) :
                movieService.discoverMovies(filters, currentPage);

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

            // Wrap the title inside a fixed container
            VBox titleContainer = new VBox(movieTitle);
            titleContainer.setPrefHeight(50);
            titleContainer.setAlignment(Pos.CENTER);

            // Create label for the movie release date
            Text releaseDate = new Text(movie.getFormattedReleaseDate());
            releaseDate.setTextAlignment(TextAlignment.CENTER);

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
            movieBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-radius: 10; -fx-border-color: #ddd; -fx-border-width: 1;");

            // Add components to movieBox
            movieBox.getChildren().addAll(imageView, titleContainer, dateContainer);

            // Add click listener for movie details
            movieBox.setOnMouseClicked(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/movieapp/popup.fxml"));
                    Parent root = loader.load();
                    PopupController popupController = loader.getController();
                    popupController.showMoviePopup(movie);

                    Scene popupScene = new Scene(root);
                    Stage popupStage = new Stage();
                    popupStage.initModality(Modality.APPLICATION_MODAL);
                    popupStage.setTitle(movie.getTitle());
                    popupStage.setScene(popupScene);
                    popupStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
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

    @FXML
    public void applyFilters() {
        // Collect user-selected filters
        currentFilters.clear();

        if (genreComboBox.getValue() != null) {
            currentFilters.put("with_genres", movieService.getGenreId(genreComboBox.getValue().toString()));
        }
        if (!yearTextField.getText().isEmpty()) {
            try {
                int year = Integer.parseInt(yearTextField.getText());
                if (year < 1900 || year > 2099) {
                    throw new NumberFormatException("Year out of range");
                }
                currentFilters.put("primary_release_year", String.valueOf(year));
            } catch (NumberFormatException e) {
                System.out.println("Invalid year: " + yearTextField.getText());
            }
        }

        // Update the UI
        currentPage = 1;
        updateResults("", currentFilters);
    }

    // Called when the next page button is clicked
    @FXML
    private void handleNextPage() {
        currentPage++;
        updateResults("", currentFilters);
    }

    // Called when the previous page button is clicked
    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            updateResults("", currentFilters);
        }
    }
}
