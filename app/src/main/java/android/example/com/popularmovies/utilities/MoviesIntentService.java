package android.example.com.popularmovies.utilities;

import android.app.IntentService;
import android.content.Intent;
import android.example.com.popularmovies.MainViewModel;
import android.example.com.popularmovies.data.Movie;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

public class MoviesIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public MoviesIntentService() {
        super("MostPopularMoviesIntentService");
        Log.d("TAG", "Fetching most popular movies 50");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("TAG", "Fetching most popular movies 51");
        String action = intent.getAction();
        MoviesTasks.executeTask(this, action);
        stopSelf();
    }
}
