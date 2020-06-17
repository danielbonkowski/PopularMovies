package android.example.com.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.MoviesIntentService;
import android.example.com.popularmovies.utilities.MostPopularMoviesTask;
import android.example.com.popularmovies.utilities.MoviesTasks;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private final static String TAG = MainViewModel.class.getSimpleName();


    private final String API_REQUEST_URL = "api_request_url";

    public static final String ACTION_GET_MOST_POPULAR_MOVIES = "get-most-popular-movies";
    public static final String ACTION_GET_TOP_RATED_MOVIES = "get-top-rated-movies";
    public static final String ACTION_RESET_FAVORITE_MOVIES = "reset-favorite-movies";

    private static final Object LOCK = new Object();


    private static MutableLiveData<Integer> spinnerPosition;
    private static LiveData<List<Movie>> favoriteMovies;
    private static MutableLiveData<List<Movie>> mostPopularMovies;
    private static MutableLiveData<List<Movie>> topRatedMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(getApplication());
        favoriteMovies = database.movieDao().loadAllMovies();
        startMostPopularMoviesService(application);
        startTopRatedMoviesService(application);
        getSpinnerPosition();
    }

    public MutableLiveData<Integer> getSpinnerPosition() {
        if(spinnerPosition == null){
            spinnerPosition = new MutableLiveData<Integer>();
        }
        return spinnerPosition;
    }

    public static void setSpinnerPosition(Integer spinnerPosition) {
        MainViewModel.spinnerPosition.postValue(spinnerPosition);
    }

    public static LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public static void setFavoriteMovies(LiveData<List<Movie>> movies){
        favoriteMovies = movies;
    }

    public static void setTopRatedMovies(List<Movie> topRatedMovies) {
        MainViewModel.topRatedMovies.postValue(topRatedMovies);
    }

    public static LiveData<List<Movie>> getMostPopularMovies() {
        if(mostPopularMovies == null){
            MainViewModel.mostPopularMovies = new MutableLiveData<List<Movie>>();
        }
        Log.d(TAG, "Fetching most popular movies 7");
        return MainViewModel.mostPopularMovies;
    }

    public static void setMostPopularMovies(List<Movie> mostPopularMovies) {
        Log.d(TAG, "Set most popular movies");
        MainViewModel.mostPopularMovies.postValue(mostPopularMovies);
    }

    public static void startMostPopularMoviesService(Context context){
        Log.d(TAG, "Fetching most popular movies 31");
        Intent mostPopularMoviesIntent = new Intent(context, MoviesIntentService.class);
        Log.d(TAG, "Fetching most popular movies 32");
        mostPopularMoviesIntent.setAction(ACTION_GET_MOST_POPULAR_MOVIES);
        Log.d(TAG, "Fetching most popular movies 33");
        context.startService(mostPopularMoviesIntent);
        Log.d(TAG, "Fetching most popular movies 34");
    }

    public static void startTopRatedMoviesService(Context context){
        Log.d(TAG, "Fetching most popular movies 31");
        Intent topRatedMoviesIntent = new Intent(context, MoviesIntentService.class);
        Log.d(TAG, "Fetching most popular movies 32");
        topRatedMoviesIntent.setAction(ACTION_GET_TOP_RATED_MOVIES);
        Log.d(TAG, "Fetching most popular movies 33");
        context.startService(topRatedMoviesIntent);
        Log.d(TAG, "Fetching most popular movies 34");
    }

    public static void startFavoriteMoviesService(Context context){
        Intent favoriteMoviesIntent = new Intent(context, MoviesIntentService.class);
        favoriteMoviesIntent.setAction(ACTION_RESET_FAVORITE_MOVIES);
        context.startService(favoriteMoviesIntent);
    }


    public LiveData<List<Movie>> getTopRatedMovies() {
        if(topRatedMovies == null){
            topRatedMovies = new MutableLiveData<List<Movie>>();
        }
        return topRatedMovies;
    }

    private LiveData<List<Movie>> fetchMostPopularMovies(){
        try{
            Log.d(TAG, "Fetching most popular movies 2");
            mostPopularMovies.postValue(new MostPopularMoviesTask().execute().get());
            MoviesTasks.executeTask(this.getApplication(), ACTION_GET_MOST_POPULAR_MOVIES);
            //return mostPopularMovies;
            return null;
        }catch (ExecutionException | InterruptedException e){
            Log.d(TAG, "Fetching most popular movies 100");
            e.printStackTrace();
            return null;
        }
    }






}
