package com.example.movieapp.controllers;

import com.example.movieapp.models.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class PopupController {

    @FXML
    public GridPane gridPane;
    @FXML
    private ImageView poster;
    @FXML
    public Label title;
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

    public void showMoviePopup(Movie movie) {
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

        // set buttons
//            like_image.setImage(new Image("../assets/LIKE.png"));
//
//            watched_image.setImage(new Image("../assets/NOT_WATCHED.png"));
//
//            towatch_image.setImage(new Image("../assets/ADDTOWATCHLIST.png"));



    }
}
