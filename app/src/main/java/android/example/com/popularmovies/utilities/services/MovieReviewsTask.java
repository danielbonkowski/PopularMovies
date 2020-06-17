package android.example.com.popularmovies.utilities.services;

import android.example.com.popularmovies.data.Review;
import android.example.com.popularmovies.utilities.MovieDatabaseJsonUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieReviewsTask extends AsyncTask<String, Void, List<Review>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Review> doInBackground(String... params) {

        String movieId = "";
        if(params != null){
            movieId = params[0];
        }

        URL reviewsSearchUrl = NetworkUtils.buildReviewsUrl(movieId);
        List<Review> reviews = null;

        try{
            String jsonReviewsResponse =
                    NetworkUtils.getResponseFromHttpUrl(reviewsSearchUrl);

            return MovieDatabaseJsonUtils.getReviewObjectsFromJson(jsonReviewsResponse);
        }catch (IOException | JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
