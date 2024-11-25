package com.example.movieapp.controllers;

import com.example.movieapp.SceneManager;
import com.example.movieapp.services.MovieService;
import com.example.movieapp.models.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

/**The controller for the search window.
 * Allows the user to search through movies with a name
 */
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
        watchListChoiceBox.getItems().addAll("Liked Movies", "To Watch", "Seen");
        watchListChoiceBox.setValue("Watch Lists");
        watchListChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> handleWatchListSelectionChange(newValue.toString()));
    }

    @FXML
    public void handleSearchSceneButton() throws IOException {
        SceneManager.switchScene(SEARCH_SCENE_PATH);
    }

    @FXML
    public void handleDiscoverSceneButton() throws IOException {
        SceneManager.switchScene(DISCOVER_SCENE_PATH);
    }

    /**Handles switching to the List scene based on the selection picked in the list dropdown
     *
     * @param selectedList the option of the dropdown selected
     */
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

    // Called when the search button is clicked
    @FXML
    public void handleSearchButton() {
        String query = searchField.getText();
        if (query != null && !query.isEmpty()) {
            currentPage = 1;
            updateResults(query);
        }
    }

    /**Updates the list of movies on screen based on the given query
     *
     * @param query the name of the movie to search for in the API
     */
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
            movieTitle.setWrappingWidth(150);
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
