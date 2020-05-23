package android.example.com.popularmovies.utilities;

import android.example.com.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MovieDatabaseJsonUtils {

    public static Movie[] getMovieObjectsFromJson(String moviesJsonStr) throws JSONException {

        final String MDB_RESULTS = "results";

        final String MDB_MOVIE_ID = "id";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_MOVIE_POSTER = "poster_path";
        final String MDB_PLOT_SYNOPSIS = "overview";
        final String MDB_USER_AVERAGE_RATING = "vote_average";
        final String MDB_RELEASE_DATE = "release_date";

        Movie[] parsedMoviesObjects = null;

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
            Date releaseDate = new Date(resultObject.getString(MDB_RELEASE_DATE));

            Movie movie = new Movie();
            parsedMoviesObjects[i] = movie;
        }

        return parsedMoviesObjects;
    }
}
