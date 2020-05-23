package android.example.com.popularmovies.data;

import android.widget.ImageView;

import java.util.Date;

public class Movie {

    private String originalTitle;
    private ImageView moviePoster;
    private String plotSynopsis;
    private Float userAverageRating;
    private Date releaseDate;


    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public ImageView getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(ImageView moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public Float getUserAverageRating() {
        return userAverageRating;
    }

    public void setUserAverageRating(Float userAverageRating) {
        this.userAverageRating = userAverageRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
