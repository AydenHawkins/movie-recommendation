package com.example.movieapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**Class to handle loading and showing scenes and swapping between them
 *
 */
public class SceneManager {
    private static Stage primaryStage;
    //listTable references the table that should be referenced when loading the List scene
    //Kept in SceneManager so it doesn't disappear when scenes are swapped
    private static String listTable;
    private static int currentMovie;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static String getListTable() {
        return listTable;
    }

    public static void setListTable(String listTable) {
        SceneManager.listTable = listTable;
    }

    public static int getCurrentMovie() { return currentMovie;}

    public static void setCurrentMovie(int currentMovie) {
        SceneManager.currentMovie = currentMovie;
    }

    /**Contains the logic for loading and switching to a given scene
     *
     * @param fxmlPath A path to a fxml file to load
     * @throws IOException If the given file cannot be found
     */
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
