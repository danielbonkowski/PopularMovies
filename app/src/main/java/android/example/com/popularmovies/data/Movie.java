package android.example.com.popularmovies.data;

import android.content.Context;
import android.example.com.popularmovies.R;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Date;

@Entity(tableName = "movie")
public class Movie implements Parcelable {


    @PrimaryKey
    private final int movieId;
    private final String originalTitle;
    private final String moviePoster;
    private final String plotSynopsis;
    private final Double userAverageRating;

    private final Date releaseDate;

    public Movie(int movieId, String originalTitle, String moviePoster,
                 String plotSynopsis, Double userAverageRating, Date releaseDate) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.moviePoster = moviePoster;
        this.plotSynopsis = plotSynopsis;
        this.userAverageRating = userAverageRating;
        this.releaseDate = releaseDate;
    }

    @Ignore
    private Movie(Parcel in) {
        movieId = in.readInt();
        originalTitle = in.readString();
        moviePoster = in.readString();
        plotSynopsis = in.readString();
        userAverageRating = in.readDouble();
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

    public int getMovieId() { return movieId; }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public Double getUserAverageRating() {
        return userAverageRating;
    }

    public String getReleaseDate(Context context) {
        String date = context.getResources().getString(R.string.unknown_release_date);

        if(releaseDate != null && releaseDate.getTime() != 0){
            date = formatDate(context);
        }

        return date;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    private String formatDate(Context context){
        DateFormat formattedDate = android.text.format.DateFormat.getDateFormat(context);
        return formattedDate.format(releaseDate);
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
        dest.writeLong(releaseDate == null? 0 : releaseDate.getTime());
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
