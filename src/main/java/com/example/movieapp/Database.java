package com.example.movieapp;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static final String url = "jdbc:sqlite:movieuserdata.db";

    public static void insert_liked_movie(int mov_id){
        String id = String.valueOf(mov_id);
        String sql = "INSERT INTO Liked_Movies VALUES(?)";

        try(var conn = DriverManager.getConnection(url);
        var stmt = conn.prepareStatement(sql)){
            stmt.setString(1, id);
            stmt.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void insert_watched_movie(){

    }

    public static void insert_watch_list(){

    }

    public static void removeLikedMovie(int movieID) {
        String ID = String.valueOf(movieID);
        String sql = "DELETE FROM Liked_Movies WHERE movie_id = ?";

        try {
            // connect to database using url
            Connection conn = DriverManager.getConnection(url);
            // prepare SQL statement with ID replacing ?
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, ID);
            // execute statement
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static void removeWatchedMovie(int movieID) {
        String ID = String.valueOf(movieID);
        String sql = "DELETE FROM Watched_Movies WHERE movie_id = ?";

        try {
            // connect to database using url
            Connection conn = DriverManager.getConnection(url);
            // prepare SQL statement with ID replacing ?
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, ID);
            // execute statement
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static void removeToWatch(int movieID) {
        String ID = String.valueOf(movieID);
        String sql = "DELETE FROM To_Watch WHERE movie_id = ?";

        try {
            // connect to database using url
            Connection conn = DriverManager.getConnection(url);
            // prepare SQL statement with ID replacing ?
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, ID);
            // execute statement
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static ArrayList<String> getLikedMovies() {
        String sql = "SELECT * FROM Liked_Movies";
        ArrayList<String> likedIDs = new ArrayList<>();

        try {
            // connect to database using url
            Connection conn = DriverManager.getConnection(url);
            // create SQL statement
            Statement statement = conn.createStatement();
            // execute query
            ResultSet queryResults = statement.executeQuery(sql);
            // iterate through ResultSet and add each id to ArrayList
            while (queryResults.next()) {
                likedIDs.add(queryResults.getString("movie_id"));
            }

        } catch (SQLException e) {
                System.out.println("Database Error: " + e.getMessage());
            }

        // return ArrayList
        return likedIDs;
    }

    public static ArrayList<String> getWatchedMovies() {
        String sql = "SELECT * FROM Watched_Movies";
        ArrayList<String> watchedIDs = new ArrayList<>();

        try {
            // connect to database using url
            Connection conn = DriverManager.getConnection(url);
            // create SQL statement
            Statement statement = conn.createStatement();
            // execute query
            ResultSet queryResults = statement.executeQuery(sql);
            // iterate through ResultSet and add each id to ArrayList
            while (queryResults.next()) {
                watchedIDs.add(queryResults.getString("movie_id"));
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        // return ArrayList
        return watchedIDs;
    }

    public static ArrayList<String> getToWatch() {
        String sql = "SELECT * FROM To_Watch";
        ArrayList<String> toWatchIDs = new ArrayList<>();

        try {
            // connect to database using url
            Connection conn = DriverManager.getConnection(url);
            // create SQL statement
            Statement statement = conn.createStatement();
            // execute query
            ResultSet queryResults = statement.executeQuery(sql);
            // iterate through ResultSet and add each id to ArrayList
            while (queryResults.next()) {
                toWatchIDs.add(queryResults.getString("movie_id"));
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        // return ArrayList
        return toWatchIDs;
    }
}

