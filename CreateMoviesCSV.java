package com.example.movie_recommendation2;

import java.io.FileWriter;
import java.io.IOException;

public class CreateMoviesCSV{
    public static void main(String[] args) {
        String filePath = "src/main/resources/movies.csv";
        String[] movies = {
                "The Shawshank Redemption,Drama, 1994",
                "The Dark Knight,Action,2008",
                "Inception,Sci-Fi,2010",
                "Forrest Gump,Drama,1994",
                "The GodFather,Crime,1972",
                "Pulp Fiction,Crime,1994",
                "The Matrix,Sci-Fi,1999",
                "Avengers: Endgame,Action,2019"
        };

        try(FileWriter writer = new FileWriter(filePath)) {
            writer.write("Title,Genre,Year\n");
            for(String movie: movies) {
                writer.write(movie + "\n");
            }
            System.out.println("movies.csv file created successfully at: " + filePath);
    } catch (IOException e) {
            System.err.println("Error while creating the file: " + filePath+"':" + e.getMessage());
        }
    }
}
