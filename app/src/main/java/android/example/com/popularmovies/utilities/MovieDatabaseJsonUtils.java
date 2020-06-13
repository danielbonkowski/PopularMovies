package android.example.com.popularmovies.utilities;

import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.data.Review;
import android.example.com.popularmovies.data.Trailer;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieDatabaseJsonUtils {

    private static String TAG = MovieDatabaseJsonUtils.class.getSimpleName();

    public static List<Movie> getMovieObjectsFromJson(String moviesJsonStr) throws JSONException {

        final String MDB_RESULTS = "results";

        final String MDB_MOVIE_ID = "id";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_MOVIE_POSTER = "poster_path";
        final String MDB_PLOT_SYNOPSIS = "overview";
        final String MDB_USER_AVERAGE_RATING = "vote_average";
        final String MDB_RELEASE_DATE = "release_date";

        List<Movie> parsedMoviesObjects = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

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

    public static List<Trailer> getTrailerObjectsFromJson(String trailersJsonStr) throws JSONException{

        final String MDB_RESULTS = "results";

        final String MDB_ID = "id";
        final String MDB_KEY = "key";
        final String MDB_NAME = "name";
        final String MDB_SITE = "site";
        final String MDB_MAX_VIDEO_QUALITY = "size";
        final String MDB_TYPE = "type";

        final String TRAILER_TYPE = "Trailer";

        List<Trailer> parsedTrailersObjects = new ArrayList<>();

        JSONObject trailersJson = new JSONObject(trailersJsonStr);

        JSONArray resultsArray = trailersJson.getJSONArray(MDB_RESULTS);

        Log.v(TAG, "My resultsArray: " + resultsArray.toString());

        for(int i = 0; i < resultsArray.length(); i++){
            JSONObject trailerJsonObject = resultsArray.getJSONObject(i);

            String type = trailerJsonObject.getString(MDB_TYPE);


            //Discards clips and only accepts trailers
            if(!type.equals(TRAILER_TYPE)){
                continue;
            }

            Log.v(TAG,  "My type: " + type);

            String id = trailerJsonObject.getString(MDB_ID);
            String key = trailerJsonObject.getString(MDB_KEY);
            String name = trailerJsonObject.getString(MDB_NAME);
            String site = trailerJsonObject.getString(MDB_SITE);
            int size = trailerJsonObject.getInt(MDB_MAX_VIDEO_QUALITY);


            Trailer trailer = new Trailer(id, key, name, site,
                    size, type);

            parsedTrailersObjects.add(trailer);
            Log.v(TAG,  "My trailer: " + trailer.toString());
        }


        return parsedTrailersObjects;
    }


    public static List<Review> getReviewObjectsFromJson(String reviewsJsonStr) throws JSONException {

        final String MDB_FILM_ID = "id";
        final String MDB_RESULTS = "results";

        final String MDB_REVIEW_ID = "id";
        final String MDB_AUTHOR = "author";
        final String MDB_CONTENT = "content";
        final String MDB_URL = "url";

        List<Review> parsedReviewsObjects = new ArrayList<>();

        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);

        //int filmId = reviewsJson.getInt(MDB_FILM_ID);
        JSONArray resultsArray = reviewsJson.getJSONArray(MDB_RESULTS);

        for(int i = 0 ; i < resultsArray.length(); i++){
            JSONObject reviewJsonObject = resultsArray.getJSONObject(i);

            String author = reviewJsonObject.getString(MDB_AUTHOR);
            String content = reviewJsonObject.getString(MDB_CONTENT);
            String id = reviewJsonObject.getString(MDB_REVIEW_ID);
            String url = reviewJsonObject.getString(MDB_URL);


            Review review = new Review(id, author, content, url);

            parsedReviewsObjects.add(review);
            Log.v(TAG,  "My trailer: " + review.toString());
        }

        return parsedReviewsObjects;
    }
}
