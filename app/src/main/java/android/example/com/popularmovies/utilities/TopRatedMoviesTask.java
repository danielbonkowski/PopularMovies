package android.example.com.popularmovies.utilities;

import android.content.Context;
import android.example.com.popularmovies.data.Movie;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TopRatedMoviesTask extends AsyncTask<Context, Void, List<Movie>> {
    private final String TAG  = TopRatedMoviesTask.class.getSimpleName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(Context... params) {

        URL moviesRequestUrl = NetworkUtils.buildTopRatedUrl();
        List<Movie> movies = null;

        Log.d(TAG, "Fetching top rated movies 83");
        try{
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(moviesRequestUrl);

            Log.d(TAG, "Fetching top rated movies 84");
            movies = MovieDatabaseJsonUtils.getMovieObjectsFromJson(jsonMoviesResponse);
            Log.d(TAG, "Fetching top rated movies 85");

            return movies;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Fetching most popular movies 700");
            return null;
        }

    }

    @Override
    protected void onPostExecute(List<Movie> moviesData) {
        super.onPostExecute(moviesData);

    }
}
