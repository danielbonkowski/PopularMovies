package android.example.com.popularmovies.utilities;

import android.content.Context;
import android.example.com.popularmovies.MainViewModel;
import android.example.com.popularmovies.data.Movie;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MoviesTasks {


    private static final String TAG = MoviesTasks.class.getSimpleName();

    public static final String ACTION_SET_MOST_POPULAR_MOVIES = "get-most-popular-movies";
    public static final String ACTION_SET_TOP_RATED_MOVIES = "get-top-rated-movies";
    public static final String ACTION_RESET_FAVORITE_MOVIES = "reset-favorite-movies";

    public static LiveData<List<Movie>> executeTask(Context context, String action){

        if(ACTION_SET_MOST_POPULAR_MOVIES.equals(action)){
            Log.d(TAG, "Fetching most popular movies 42");
             setMostPopularMovies(context);
        }else if(ACTION_SET_TOP_RATED_MOVIES.equals(action)){
             setTopRatedMovies(context);
            Log.d(TAG, "Fetching top rated movies 42");
        }else if (ACTION_RESET_FAVORITE_MOVIES.equals(action)){
            Log.d(TAG, "Fetching favorite movies 42");
            resetFavoriteMovies(context);
        }
        return null;
    }

    private static void setTopRatedMovies(Context context) {
        try{
            Log.d(TAG, "Fetching top rated movies 93");
            List<Movie> topRatedMovies = new TopRatedMoviesTask().execute().get();
            MainViewModel.setTopRatedMovies(topRatedMovies);
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private static void setMostPopularMovies(Context context){
        try{
            Log.d(TAG, "Fetching most popular movies 43");
            List<Movie> mostPopularMovies = new MostPopularMoviesTask().execute().get();
            MainViewModel.setMostPopularMovies(mostPopularMovies);
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private static void resetFavoriteMovies(Context context){
        Log.d(TAG, "Reset favorite movies 43");
        LiveData<List<Movie>> favoriteMovies =  MainViewModel.getFavoriteMovies();
        MainViewModel.setFavoriteMovies(null);
        MainViewModel.setFavoriteMovies(favoriteMovies);
    }
}
