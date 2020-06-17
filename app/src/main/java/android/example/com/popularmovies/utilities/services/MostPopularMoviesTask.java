package android.example.com.popularmovies.utilities.services;

import android.content.Context;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.MovieDatabaseJsonUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MostPopularMoviesTask extends AsyncTask<Context, Void, List<Movie>> {

    @Override
    protected List<Movie> doInBackground(Context... params) {

        URL moviesRequestUrl = NetworkUtils.buildPopularityUrl();

        try{
            String jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(moviesRequestUrl);

            return MovieDatabaseJsonUtils.getMovieObjectsFromJson(jsonMoviesResponse);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
