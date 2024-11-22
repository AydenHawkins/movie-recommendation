package com.example.movieapp.models;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    @SerializedName("overview")
    private String overview;
    @SerializedName("vote_average")
    private float voteAverage;

    @Override
    public String toString() {
        return String.format(title + " (" + releaseDate + ") - Popularity: " + popularity);
    }

    public String getFormattedReleaseDate() {
        if (releaseDate == null || releaseDate.isEmpty()) {
            return "Unknown Release Date";
        }
        try {
            LocalDate date = LocalDate.parse(releaseDate);
            return date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        } catch (DateTimeParseException e) {
            return "Invalid Date Format";
        }
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

    public String getOverview() {return overview;}

    public float getVoteAverage() {return voteAverage;}

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

    public void setOverview(String overview) { this.overview = overview; }

    public void setVoteAverage(float voteAverage) { this.voteAverage = voteAverage; }

}
