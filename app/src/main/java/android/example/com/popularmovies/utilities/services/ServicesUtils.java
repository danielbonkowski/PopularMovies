package android.example.com.popularmovies.utilities.services;

import android.content.Context;
import android.content.Intent;

public class ServicesUtils {

    public static final String ACTION_SET_MOST_POPULAR_MOVIES = "get-most-popular-movies";
    public static final String ACTION_SET_TOP_RATED_MOVIES = "get-top-rated-movies";
    public static final String ACTION_SET_MOVIE_TRAILERS = "get-movie-trailers";
    public static final String ACTION_SET_MOVIE_REVIEWS = "get-movies-reviews";
    public static final String PARAM_MOVIE_ID = "movie-id";


    public static void startMostPopularMoviesService(Context context){
        Intent mostPopularMoviesIntent = new Intent(context, MoviesIntentService.class);
        mostPopularMoviesIntent.setAction(ACTION_SET_MOST_POPULAR_MOVIES);
        context.startService(mostPopularMoviesIntent);
    }

    public static void startTopRatedMoviesService(Context context){
        Intent topRatedMoviesIntent = new Intent(context, MoviesIntentService.class);
        topRatedMoviesIntent.setAction(ACTION_SET_TOP_RATED_MOVIES);
        context.startService(topRatedMoviesIntent);
    }

    public static void startTrailersService(Context context, String movieId){
        Intent trailersIntent = new Intent(context, MoviesIntentService.class);
        trailersIntent.setAction(ACTION_SET_MOVIE_TRAILERS);
        trailersIntent.putExtra(PARAM_MOVIE_ID, movieId);
        context.startService(trailersIntent);
    }

    public static void startReviewsService(Context context, String movieId){
        Intent reviewsIntent = new Intent(context, MoviesIntentService.class);
        reviewsIntent.setAction(ACTION_SET_MOVIE_REVIEWS);
        reviewsIntent.putExtra(PARAM_MOVIE_ID, movieId);
        context.startService(reviewsIntent);
    }
}
