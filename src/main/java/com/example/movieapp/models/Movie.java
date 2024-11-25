package com.example.movieapp.models;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**Class for movie objects
 *
 */
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
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("revenue")
    private double revenue;
    @SerializedName("budget")
    private double budget;
    @SerializedName("genres")
    private List<Genre> genres;

    @Override
    public String toString() {
        return String.format(title + " (" + releaseDate + ") - Popularity: " + popularity);
    }

    /**returns the release date in a MM DD, YYYY format
     *
     * @return A string of the formatted release date
     */
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

    public List<Genre> getGenres() {
        return genres;
    }

    public double getBudget() {
        return budget;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
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

    public void setId(int id) {this.id = id;}

    public void setPosterPath(String posterPath) { this.posterPath = posterPath;}

    public void setOverview(String overview) { this.overview = overview; }

    public void setVoteAverage(float voteAverage) { this.voteAverage = voteAverage; }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
