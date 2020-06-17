package android.example.com.popularmovies.utilities.services;

import android.content.Context;
import android.example.com.popularmovies.CheckMovieViewModel;
import android.example.com.popularmovies.MainViewModel;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.data.Trailer;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MoviesTasks {


    private static final String TAG = MoviesTasks.class.getSimpleName();

    public static LiveData<List<Movie>> executeTask(Context context, String action){

        if(ServicesUtils.ACTION_SET_MOST_POPULAR_MOVIES.equals(action)){
            Log.d(TAG, "Fetching most popular movies 42");
             setMostPopularMovies(context);
        }else if(ServicesUtils.ACTION_SET_TOP_RATED_MOVIES.equals(action)){
             setTopRatedMovies(context);
            Log.d(TAG, "Fetching top rated movies 42");
        }
        return null;
    }

    public static LiveData<List<Movie>> executeTask(Context context,
                                                    String action, String movieId){

        if(ServicesUtils.ACTION_SET_MOVIE_TRAILERS.equals(action)){
            setMovieTrailers(context, movieId);
        }else if(ServicesUtils.ACTION_SET_MOVIE_REVIEWS.equals(action)){

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

    private static void setMovieTrailers(Context context, String movieId){
        try{
            List<Trailer> movieTrailers = new MovieTrailersTask().execute(movieId).get();
            CheckMovieViewModel.setTrailers(movieTrailers);
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }

    }

    private static void setMovieReviews(Context context){

    }

}
