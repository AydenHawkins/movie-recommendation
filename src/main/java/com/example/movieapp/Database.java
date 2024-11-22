package com.example.movieapp;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static final String url = "jdbc:sqlite:movieuserdata.db";

    /** Takes a movieID and inserts it into the database into table Liked_Movie
     *
     * @param movieID the ID for the movie
     */
    public static void insertLikedMovie(int movieID){
        //api calls return movieID as an int so easier to cast within the method
        String id = String.valueOf(movieID);
        String sql = "INSERT INTO Liked_Movies VALUES(?)";

        try(var conn = DriverManager.getConnection(url);
        var stmt = conn.prepareStatement(sql)){
            //putting the movie ID into the ? parameter in the sql statement
            stmt.setString(1, id);
            stmt.execute();
        }catch(SQLException e){
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Takes a movieID and inserts it into the database into table Watched_Movie
     *
     * @param movieID the ID for the movie
     */
    public static void insertWatchedMovie(int movieID){
        //api calls return movieID as an int so easier to cast within the method
        String id = String.valueOf(movieID);
        String sql = "INSERT INTO Watched_Movies VALUES(?)";

        try(var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql)){
            //putting the movie ID into the ? parameter in the sql statement
            stmt.setString(1, id);
            stmt.execute();
        }catch(SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Takes a movieID and inserts it into the database into table To_Watch
     *
     * @param movieID the ID for the movie
     */
    public static void insertWatchList(int movieID){
        //api calls return movieID as an int so easier to cast within the method
        String id = String.valueOf(movieID);
        String sql = "INSERT INTO To_Watch VALUES(?)";

        try(var conn = DriverManager.getConnection(url);
            var stmt = conn.prepareStatement(sql)){
            //putting the movie ID into the ? parameter in the sql statement
            stmt.setString(1, id);
            stmt.execute();
        }catch(SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**removes given movieID from the Liked_Movies database
     *
     * @param movieID the ID of the movie to remove
     */
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

    /**removes given movieID from the Watched_Movies database
     *
     * @param movieID the ID of the movie to remove
     */
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

    /**removes given movieID from the To_Watch database
     *
     * @param movieID the ID of the movie to remove
     */
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

    /** returns an ArrayList consisting of all movieIDs in the Liked_Movies table
     *
     * @return ArrayList of all movieIDs
     */
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

    /** returns an ArrayList consisting of all movieIDs in the Watched_Movies table
     *
     * @return ArrayList of all movieIDs
     */
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

    /** returns an ArrayList consisting of all movieIDs in the To_Watch table
     *
     * @return ArrayList of all movieIDs
     */
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

