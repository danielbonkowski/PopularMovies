package android.example.com.popularmovies.utilities;

import android.example.com.popularmovies.data.Movie;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MovieDatabaseJsonUtils {

    public static List<Movie> getMovieObjectsFromJson(String moviesJsonStr) throws JSONException {

        final String MDB_RESULTS = "results";

        final String MDB_MOVIE_ID = "id";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_MOVIE_POSTER = "poster_path";
        final String MDB_PLOT_SYNOPSIS = "overview";
        final String MDB_USER_AVERAGE_RATING = "vote_average";
        final String MDB_RELEASE_DATE = "release_date";

        List<Movie> parsedMoviesObjects = new ArrayList<Movie>();

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        if(moviesJson == null){
            return null;
        }

        JSONArray resultsArray = moviesJson.getJSONArray(MDB_RESULTS);

        for(int i = 0; i<  resultsArray.length(); i++){

            JSONObject resultObject = resultsArray.getJSONObject(i);

            int movieId = resultObject.getInt(MDB_MOVIE_ID);
            String originalTitle = resultObject.getString(MDB_ORIGINAL_TITLE);
            String moviePoster = resultObject.getString(MDB_MOVIE_POSTER);
            String plotSynopsis = resultObject.getString(MDB_PLOT_SYNOPSIS);
            Double userAverageRating = resultObject.getDouble(MDB_USER_AVERAGE_RATING);
            String dateString = resultObject.getString(MDB_RELEASE_DATE);

            Date releaseDate = null;
            try {
                releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            }catch (ParseException e){
                e.getStackTrace();
            }

            Movie movie = new Movie(movieId, originalTitle, moviePoster,
                    plotSynopsis, userAverageRating, releaseDate);
            parsedMoviesObjects.add(movie);
        }

        return parsedMoviesObjects;
    }
}
