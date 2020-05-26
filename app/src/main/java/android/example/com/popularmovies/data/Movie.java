package android.example.com.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Movie implements Parcelable {

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


    private Movie(Parcel in) {
        movieId = in.readInt();
        originalTitle = in.readString();
        moviePoster = in.readString();
        plotSynopsis = in.readString();
        if (in.readByte() == 0) {
            userAverageRating = null;
        } else {
            userAverageRating = in.readDouble();
        }
        releaseDate = new Date(in.readLong());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(originalTitle);
        dest.writeString(moviePoster);
        dest.writeString(plotSynopsis);
        dest.writeDouble(userAverageRating);
        dest.writeLong(releaseDate.getTime());
    }

    @NonNull
    @Override
    public String toString() {
        return "Id: " + movieId +
                "\nOriginal title: " + originalTitle +
                "\nMovie poster: "  + moviePoster +
                "\nPlot synopsis: " + plotSynopsis +
                "\nUser average rating: " + userAverageRating +
                "\nRelease date: " + releaseDate;
    }
}
