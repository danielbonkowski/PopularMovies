package android.example.com.popularmovies.utilities.services;

import android.example.com.popularmovies.data.Trailer;
import android.example.com.popularmovies.utilities.MovieDatabaseJsonUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieTrailersTask extends AsyncTask<String, Void, List<Trailer>> {

    @Override
    protected List<Trailer> doInBackground(String... params) {

        String movieId = "";
        if(params != null){
            movieId = params[0];
        }

        URL trailersSearchUrl = NetworkUtils.buildTrailersUrl(movieId);

        try{
            String jsonTrailersResponse =
                    NetworkUtils.getResponseFromHttpUrl(trailersSearchUrl);

            return MovieDatabaseJsonUtils.getTrailerObjectsFromJson(jsonTrailersResponse);
        }catch (IOException | JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
