package com.example.movieapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    private static Stage primaryStage;

    private String listTable;

    public static void setListTable(String listTable){
        this.listTable = listTable;
    }

    public static String getListTable(){
        return listTable;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource(fxmlPath)));
        Scene scene = new Scene(root);

        // Get the screen size
        Screen primaryScreen = Screen.getPrimary();
        double screenWidth = primaryScreen.getVisualBounds().getWidth();
        double screenHeight = primaryScreen.getVisualBounds().getHeight();

        // Set the maximum window size to the screen size
        primaryStage.setMaxWidth(screenWidth);
        primaryStage.setMaxHeight(screenHeight);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
