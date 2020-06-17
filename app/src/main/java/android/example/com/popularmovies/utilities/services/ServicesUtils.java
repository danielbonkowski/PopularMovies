package android.example.com.popularmovies.utilities.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServicesUtils {

    public static final String ACTION_GET_MOST_POPULAR_MOVIES = "get-most-popular-movies";
    public static final String ACTION_GET_TOP_RATED_MOVIES = "get-top-rated-movies";


    public static void startMostPopularMoviesService(Context context){
        Intent mostPopularMoviesIntent = new Intent(context, MoviesIntentService.class);
        mostPopularMoviesIntent.setAction(ACTION_GET_MOST_POPULAR_MOVIES);
        context.startService(mostPopularMoviesIntent);
    }

    public static void startTopRatedMoviesService(Context context){
        Intent topRatedMoviesIntent = new Intent(context, MoviesIntentService.class);
        topRatedMoviesIntent.setAction(ACTION_GET_TOP_RATED_MOVIES);
        context.startService(topRatedMoviesIntent);
    }
}
