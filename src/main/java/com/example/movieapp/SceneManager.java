package com.example.movieapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource(fxmlPath)));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setMaximized(true);
    }
}
