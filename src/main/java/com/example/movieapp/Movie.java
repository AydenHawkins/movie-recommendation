package com.example.movieapp;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Movie {
    // Instance variables
    @SerializedName("title")
    private String title;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("id")
    private int id;
    @SerializedName("poster_path")
    private String posterPath;

    @Override
    public String toString() {
        return String.format(title + " (" + releaseDate + ") - Popularity: " + popularity);
    }

    public String getFormattedReleaseDate() {
        LocalDate date = LocalDate.parse(releaseDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        return date.format(formatter);
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getPopularity() {
        return popularity;
    }

    public int getId() {return id;}

    public String getPosterPath() {return posterPath;}

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setId(int id) {this.id = id;}

    public void setPosterPath(String posterPath) { this.posterPath = posterPath;}
}
