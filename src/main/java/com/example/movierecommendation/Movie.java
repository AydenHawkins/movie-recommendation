package com.example.movierecommendation;

import com.google.gson.annotations.SerializedName;

public class Movie {
    // Instance variables
    private String title;

    @SerializedName("release_date")
    private String releaseDate;

    private float popularity;
    private String rating;
    private float duration;
    private String description;
    private String director;
    private String[] cast;

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

    public String[] getCast() {
        return cast;
    }

    public String getDirector() {
        return director;
    }

    public String getDescription() {
        return description;
    }

    public float getDuration() {
        return duration;
    }

    public String getRating() {
        return rating;
    }

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

    public void setCast(String[] cast) {
        this.cast = cast;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
