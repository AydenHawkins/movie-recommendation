package com.example.movieapp.controllers;

import com.example.movieapp.SceneManager;
import com.example.movieapp.models.Genre;
import com.example.movieapp.models.Movie;
import com.example.movieapp.services.MovieService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.movieapp.database.Database;

/**Controller for the window that handles looking through the user's lists
 *
 */
public class ListController {
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Button searchSceneButton;
    @FXML
    public ChoiceBox watchListChoiceBox;
    @FXML
    public Button discoverSceneButton;
    @FXML
    public ComboBox genreComboBox;
    @FXML
    public TextField yearTextField;
    @FXML
    private GridPane resultsGrid;

    private final MovieService movieService = new MovieService();
    private static final String SEARCH_SCENE_PATH = "/com/example/movieapp/search.fxml";
    private static final String DISCOVER_SCENE_PATH = "/com/example/movieapp/discover.fxml";
    private static final String LIST_SCENE_PATH = "/com/example/movieapp/list.fxml";
    private Map<String, String> currentFilters = new HashMap<>();

    private Map<String, Integer> genreNameToIdMap = new HashMap<>();

    private void initializeGenreMapping() {
        // Mapping of genre names to genre IDs (example)
        genreNameToIdMap.put("Action", 28);
        genreNameToIdMap.put("Comedy", 35);
        genreNameToIdMap.put("Drama", 18);
        genreNameToIdMap.put("Horror", 27);
        genreNameToIdMap.put("Science Fiction", 878);
    }


    @FXML
    public void initialize() {
        watchListChoiceBox.getItems().addAll("Liked Movies", "To Watch", "Seen");
        watchListChoiceBox.setValue("Watch Lists");
        watchListChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> handleWatchListSelectionChange(newValue.toString()));
        genreComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Science Fiction");

        updateResults(SceneManager.getListTable(), currentFilters);
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

    @FXML
    public void handleApplyFiltersButton() throws IOException {
        applyFilters();
    }

    private void updateResults(String database, Map<String, String> filters) {
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
            movies.add(MovieService.getMovieByID(String.valueOf(movieID)));
        }

        int row = 0;
        int col = 0;

        List<Movie> filteredMovies = new ArrayList<>(movies);

        if (!filters.isEmpty()) {
            filteredMovies.removeIf(movie -> {
                for (Map.Entry<String, String> entry : filters.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    switch (key) {
                        case "with_genres":
                            boolean genreMatch = false;

                            // Parse the selected genre ID from the filter value
                            Integer selectedGenreId = Integer.parseInt(value);

                            // Check if the movie's genres contain the selected genre ID
                            for (Genre genre : movie.getGenres()) {

                                if (genre.getId() == selectedGenreId) {
                                    genreMatch = true;
                                    break;
                                }
                            }

                            // If no matching genre, remove the movie
                            if (!genreMatch) {
                                return true; // Remove this movie from the filtered list
                            }
                            break;

                        case "primary_release_year":
                            if (!movie.getReleaseDate().substring(0, 4).equals(value)) {
                                return true; // Remove this movie from the filtered list
                            }
                            break;
                    }
                }
                return false; // Keep this movie in the filtered list
            });
        }

        // Add movie posters, titles, and release dates to the grid
        for (Movie movie : filteredMovies) {
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
                    popupStage.setResizable(false);
                    popupStage.setMaximized(false);
                    popupStage.setIconified(false);
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
    }

    @FXML
    public void applyFilters() {
        // Collect user-selected filters
        currentFilters.clear();

        if (genreComboBox.getValue() != null) {
            String genreName = genreComboBox.getValue().toString();
            String genreId = movieService.getGenreId(genreName);
            System.out.println("Selected Genre: " + genreName + " -> Genre ID: " + genreId);

            currentFilters.put("with_genres", genreId);
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

        System.out.println("Filters after application: " + currentFilters);

        // Update the UI with filtered results
        updateResults(SceneManager.getListTable(), currentFilters);
    }


}
