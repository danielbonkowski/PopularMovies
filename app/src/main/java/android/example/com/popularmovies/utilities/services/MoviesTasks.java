package android.example.com.popularmovies.utilities.services;

import android.example.com.popularmovies.data.model.DetailsViewModel;
import android.example.com.popularmovies.data.model.MainViewModel;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.data.Review;
import android.example.com.popularmovies.data.Trailer;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("SameReturnValue")
public class MoviesTasks {

    public static void executeTask(String action){

        if(ServicesUtils.ACTION_SET_MOST_POPULAR_MOVIES.equals(action)){
             setMostPopularMovies();
        }else if(ServicesUtils.ACTION_SET_TOP_RATED_MOVIES.equals(action)){
             setTopRatedMovies();
        }
    }

    public static void executeTask(String action, String movieId){

        if(ServicesUtils.ACTION_SET_MOVIE_TRAILERS.equals(action)){
            setMovieTrailers(movieId);
        }else if(ServicesUtils.ACTION_SET_MOVIE_REVIEWS.equals(action)){
            setMovieReviews(movieId);
        }
    }

    private static void setTopRatedMovies() {
        try{
            List<Movie> topRatedMovies = new TopRatedMoviesTask().execute().get();
            MainViewModel.setTopRatedMovies(topRatedMovies);
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private static void setMostPopularMovies(){
        try{
            List<Movie> mostPopularMovies = new MostPopularMoviesTask().execute().get();
            MainViewModel.setMostPopularMovies(mostPopularMovies);
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private static void setMovieTrailers(String movieId){
        try{
            List<Trailer> movieTrailers = new MovieTrailersTask().execute(movieId).get();
            DetailsViewModel.setTrailers(movieTrailers);
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }

    }

    private static void setMovieReviews(String movieId){
        try{
            List<Review> movieReviews = new MovieReviewsTask().execute(movieId).get();
            DetailsViewModel.setReviews(movieReviews);
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

}
