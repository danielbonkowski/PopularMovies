package android.example.com.popularmovies.utilities.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class MoviesIntentService extends IntentService {

    public MoviesIntentService() {
        super("MostPopularMoviesIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String action = intent.getAction();
        String movieId = intent.getStringExtra(ServicesUtils.PARAM_MOVIE_ID);

        if(movieId == null){
            MoviesTasks.executeTask(action);
        }else{
            MoviesTasks.executeTask(action, movieId);
        }

        stopSelf();
    }
}
