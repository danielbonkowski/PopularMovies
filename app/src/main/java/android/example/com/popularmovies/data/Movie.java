package android.example.com.popularmovies.data;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Serializable {

    private int movieId;
    private String originalTitle;
    private String moviePoster;
    private String plotSynopsis;
    private Double userAverageRating;
    private Date releaseDate;

    public Movie(int movieId, String originalTitle, String moviePoster,
                 String plotSynopsis, Double userAverageRating, Date releaseDate) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.moviePoster = moviePoster;
        this.plotSynopsis = plotSynopsis;
        this.userAverageRating = userAverageRating;
        this.releaseDate = releaseDate;
    }


    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public Double getUserAverageRating() {
        return userAverageRating;
    }

    public void setUserAverageRating(Double userAverageRating) {
        this.userAverageRating = userAverageRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
