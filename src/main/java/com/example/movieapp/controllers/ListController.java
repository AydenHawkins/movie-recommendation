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
import javafx.scene.paint.Color;
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
    public ComboBox watchListComboBox;
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
    private static final Map<String, String> GENRE_MAP = Map.ofEntries(
            Map.entry("Action", "28"),
            Map.entry("Adventure", "12"),
            Map.entry("Animation", "16"),
            Map.entry("Comedy", "35"),
            Map.entry("Crime", "80"),
            Map.entry("Documentary", "99"),
            Map.entry("Drama", "18"),
            Map.entry("Family", "10751"),
            Map.entry("Fantasy", "14"),
            Map.entry("History", "36"),
            Map.entry("Horror", "27"),
            Map.entry("Music", "10402"),
            Map.entry("Mystery", "9648"),
            Map.entry("Romance", "10749"),
            Map.entry("Science Fiction", "878"),
            Map.entry("TV Movie", "10770"),
            Map.entry("Thriller", "53"),
            Map.entry("War", "10752"),
            Map.entry("Western", "37")
    );

    /**
     * Initializes window with filters and watch list options.
     */
    @FXML
    public void initialize() {
        watchListComboBox.getItems().addAll("Liked Movies", "To Watch", "Seen");
        watchListComboBox.setValue("Watch Lists");
        watchListComboBox.setStyle("-fx-text-fill: white; -fx-background-color: #424242; -fx-border-color: #474545;");
        watchListComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> handleWatchListSelectionChange(newValue.toString()));
        genreComboBox.getItems().addAll(GENRE_MAP.keySet());

        //automatically displays results based on what the user clicked on in the list drop down
        updateResults(SceneManager.getListTable(), currentFilters);
    }

    /**
     * Handles switching to search scene.
     * @throws IOException
     */
    @FXML
    public void handleSearchSceneButton() throws IOException {
        SceneManager.switchScene(SEARCH_SCENE_PATH);
    }
    /**
     * Handles switching to discover scene.
     * @throws IOException
     */
    @FXML
    public void handleDiscoverSceneButton() throws IOException {
        SceneManager.switchScene(DISCOVER_SCENE_PATH);
    }

    /**Handles switching to the List scene based on the selection picked in the list dropdown
     *
     * @param selectedList the option of the dropdown selected
     */
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
    /**
     * Handles applying filters.
     * @throws IOException
     */
    @FXML
    public void handleApplyFiltersButton() throws IOException {
        applyFilters();
    }

    /**updates results on screen based on the given database and filters
     *
     * @param database the list of movies to pull from in movieuserdata.db
     * @param filters the filters to apply to the list of movies before displaying
     */
    private void updateResults(String database, Map<String, String> filters) {
        // Clear previous results
        resultsGrid.getChildren().clear();

        ArrayList<Integer> movieIDs = new ArrayList<>();
        // Get the movie data from database.
        switch (database) {
            case "Liked_Movies" -> movieIDs = Database.getLikedMovies();
            case "Watched_Movies" -> movieIDs = Database.getWatchedMovies();
            case "To_Watch" -> movieIDs = Database.getToWatch();
        }

        // Send HTTP request to API to get movies from IDs.
        List<Movie> movies = new ArrayList<>();
        for (int movieID : movieIDs){
            movies.add(MovieService.getMovieByID(String.valueOf(movieID)));
        }

        int row = 0;
        int col = 0;

        List<Movie> filteredMovies = new ArrayList<>(movies);

        // Filters movies based on filters selected by user.
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

                            // Remove the movie if it doesn't match one of the filters.
                            if (!genreMatch) {
                                return true;
                            }
                            break;

                        case "primary_release_year":
                            if (!movie.getReleaseDate().substring(0, 4).equals(value)) {
                                return true;
                            }
                            break;
                    }
                }
                return false;
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
            movieTitle.setWrappingWidth(150);
            movieTitle.setTextAlignment(TextAlignment.CENTER);
            movieTitle.setStyle("-fx-font-weight: bold;");
            movieTitle.setFill(Color.WHITE);

            // Wrap the title inside a fixed container.
            VBox titleContainer = new VBox(movieTitle);
            titleContainer.setPrefHeight(50);
            titleContainer.setAlignment(Pos.CENTER);

            // Create label for the movie release date
            Text releaseDate = new Text(movie.getFormattedReleaseDate());
            releaseDate.setTextAlignment(TextAlignment.CENTER);
            releaseDate.setFill(Color.WHITE);

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
            movieBox.setStyle("-fx-background-color: #424242; -fx-padding: 10; -fx-border-radius: 5; -fx-border-color: #474545; -fx-border-width: 1;");

            // Add components to movieBox
            movieBox.getChildren().addAll(imageView, titleContainer, dateContainer);

            // Initiate popup window when a movie is clicked.
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

        // Update the UI with filtered results
        updateResults(SceneManager.getListTable(), currentFilters);
    }


}
