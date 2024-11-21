package com.example.movieapp;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String url = "jdbc:sqlite:movieuserdata.db";

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
}
