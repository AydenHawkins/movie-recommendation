package com.example.movieapp.controllers;

import com.example.movieapp.SceneManager;
import com.example.movieapp.models.Movie;
import com.example.movieapp.services.MovieService;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import com.example.movieapp.database.Database;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

/**Controller class for the pop-up window
 * pops up when a user clicks on a movie in another scene to give more details about it
 */

public class PopupController {

    @FXML
    public GridPane gridPane;
    @FXML
    private ImageView poster;
    @FXML
    public Label title;
    @FXML
    public Button recommend;
    @FXML
    public Label mpaarating;
    @FXML
    public Label releasedate;
    @FXML
    public Label runtime;
    @FXML
    public Label genres;
    @FXML
    public Label userscore;
    @FXML
    public Label synopsis;
    @FXML
    public Label director;
    @FXML
    public Label cast;
    @FXML
    public Button like_button;
    @FXML
    public Button watched_button;
    @FXML
    public Button towatchlist_button;
    @FXML
    public ImageView like_image;
    @FXML
    public ImageView watched_image;
    @FXML
    public ImageView towatch_image;
    @FXML
    public ImageView providerlogo1;
    @FXML
    public ImageView providerlogo2;
    @FXML
    public ImageView providerlogo3;

    private Movie cur_movie;
    private final MovieService movieService = new MovieService();
    private Stage popUpStage;

    public void getMovieDetails(int movieID, Stage popUpStage) {
        Movie movie = movieService.getMovieByID(String.valueOf(movieID));
        showMoviePopup(movie, popUpStage);
    }

    public void showMoviePopup(Movie movie, Stage popUpStage) {
        // set movie instance variable
        this.cur_movie = movie;
        this.popUpStage = popUpStage;

        // set poster image
        poster.setImage(new Image("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()));

        // set title
        title.setText(movie.getTitle());

        // set release date
        releasedate.setText(movie.getReleaseDate());

        // set user rating
        userscore.setText(String.valueOf(movie.getVoteAverage()));

        // set overview
        synopsis.setText(movie.getOverview());

        // set runtime
        int hours = movie.getRuntime() % 60;
        int minutes = movie.getRuntime() / 60;
        runtime.setText(String.format("%dh %dm", hours, minutes));

        // set genres
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            // limit to 5 genres; append each genre to stringbuilder
            if (movie.getGenres().size() <= i) { break; }
            sb.append(movie.getGenres().get(i).getName());

            // if not last genre, add a comma and space
            if ( i == movie.getGenres().size() - 1 || i == 4) { break; }
            sb.append(", ");
        }
        genres.setText(sb.toString());

        String certification = movieService.getCertificationByMovieId(movie.getId());
        mpaarating.setText(certification.equals("N/A") ? "Unrated" : certification);


        // set buttons
        boolean inLikedMovies = Database.getLikedMovies().contains(movie.getId());
        boolean inWatchedMovies = Database.getWatchedMovies().contains(movie.getId());
        boolean inWatchList = Database.getToWatch().contains(movie.getId());

        // if movie is in liked list
        if (inLikedMovies) {
            like_image.setImage(new Image(getClass().getResource("/images/LIKED.png").toString(), true));
        } else {
            like_image.setImage(new Image(getClass().getResource("/images/LIKE.png").toString(), true));
        }

        // if movie is in watched movies list
        if (inWatchedMovies) {
            watched_image.setImage(new Image(getClass().getResource("/images/WATCHED.png").toString(), true));
        } else {
            watched_image.setImage(new Image(getClass().getResource("/images/NOT_WATCHED.png").toString(), true));
        }

        // if movie is in ToWatch list
        if (inWatchList) {
            towatch_image.setImage(new Image(getClass().getResource("/images/ONWATCHLIST.png").toString(), true));
        } else {
            towatch_image.setImage(new Image(getClass().getResource("/images/ADDTOWATCHLIST.png").toString(), true));
        }
    }

    public void setProviderLogos(int movieId) {
        List<String> logos = movieService.getProviderLogos(movieId);

        // Create an array of ImageView objects for convenience
        ImageView[] logoViews = {providerlogo1, providerlogo2, providerlogo3};

        for (int i = 0; i < logoViews.length; i++) {
            if (logos.get(i) != null) {
                // Load the image and set it to the ImageView
                logoViews[i].setImage(new Image(logos.get(i)));
            } else {
                // Clear the ImageView if no logo is available
                logoViews[i].setImage(null);
            }
        }
    }

    @FXML
    public void changeLikedList(ActionEvent event) {
        // if movie is already in liked movies relation
        if (Database.getLikedMovies().contains(cur_movie.getId())) {
            // delete from relation
            Database.removeLikedMovie(cur_movie.getId());
            // change button image
            like_image.setImage(new Image(getClass().getResource("/images/LIKE.png").toString()));
        } else {
            // if movie is not already in liked movies relation
            // insert into relation
            Database.insertLikedMovie(cur_movie.getId());
            // change button image
            like_image.setImage(new Image(getClass().getResource("/images/LIKED.png").toString()));

        }
    }

    @FXML
    public void changeWatchedList(ActionEvent event) {
        // if movie is already in watched movies relation
        if (Database.getWatchedMovies().contains(cur_movie.getId())) {
            // delete from relation
            Database.removeWatchedMovie(cur_movie.getId());
            // change button image
            watched_image.setImage(new Image(getClass().getResource("/images/NOT_WATCHED.png").toString()));
        } else {
            // if movie is not already in watched movies relation
            // insert into relation
            Database.insertWatchedMovie(cur_movie.getId());
            // change button image
            watched_image.setImage(new Image(getClass().getResource("/images/WATCHED.png").toString()));
        }
    }

    @FXML
    public void changeToWatchList(ActionEvent event) {
        // if movie is already in towatch relation
        if (Database.getToWatch().contains(cur_movie.getId())) {
            // delete from relation
            Database.removeToWatch(cur_movie.getId());
            // change button image
            towatch_image.setImage(new Image(getClass().getResource("/images/ADDTOWATCHLIST.png").toString()));
        } else {
            // if movie is not already in towatch relation
            // insert into relation
            Database.insertWatchList(cur_movie.getId());
            // change button image
            towatch_image.setImage(new Image(getClass().getResource("/images/ONWATCHLIST.png").toString()));
        }
    }

    @FXML
    public void getRecommendations(ActionEvent event) {
        try {
            SceneManager.switchScene("/com/example/movieapp/recommendation.fxml");
            popUpStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cursorToHand(MouseEvent event) {
        like_button.setCursor(Cursor.HAND);
        watched_button.setCursor(Cursor.HAND);
        towatchlist_button.setCursor(Cursor.HAND);
    }

    @FXML
    public void cursorToDefault(MouseEvent event) {
        like_button.setCursor(Cursor.DEFAULT);
        watched_button.setCursor(Cursor.DEFAULT);
        towatchlist_button.setCursor(Cursor.DEFAULT);
    }
}
