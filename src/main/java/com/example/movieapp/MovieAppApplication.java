package com.example.movieapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class MovieAppApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.switchScene("/com/example/movieapp/discover.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
