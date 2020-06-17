package android.example.com.popularmovies.utilities.services;

import android.content.Context;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.MovieDatabaseJsonUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MostPopularMoviesTask extends AsyncTask<Context, Void, List<Movie>> {

    private final String TAG  = MostPopularMoviesTask.class.getSimpleName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(Context... params) {

        URL moviesRequestUrl = NetworkUtils.buildPopularityUrl();
        List<Movie> movies = null;

        Log.d(TAG, "Fetching most popular movies 3");
        try{
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(moviesRequestUrl);

            Log.d(TAG, "Fetching most popular movies 4");
            movies = MovieDatabaseJsonUtils.getMovieObjectsFromJson(jsonMoviesResponse);
            Log.d(TAG, "Fetching most popular movies 5");

            Log.d(TAG, "Fetching most popular movies 7");
            return movies;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Fetching most popular movies 200");
            return null;
        }

    }

    @Override
    protected void onPostExecute(List<Movie> moviesData) {
        super.onPostExecute(moviesData);

    }
}
