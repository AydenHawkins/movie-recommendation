package com.example.movieapp;

public class Database {

    public static void insert_liked_movie(int id){
        String url = "jdbc:sqlite:movieuserdata.db";

        String sql = String.format("INSERT INTO Liked_Movies VALUES(%d);", id);
    }

    public static void insert_watched_movie(){

    }

    public static void insert_watch_list(){

    }
}
