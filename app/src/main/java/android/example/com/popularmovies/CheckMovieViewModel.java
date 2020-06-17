package android.example.com.popularmovies;

import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.data.Review;
import android.example.com.popularmovies.data.Trailer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CheckMovieViewModel extends ViewModel {

    private static LiveData<Movie> movie;
    private static MutableLiveData<List<Trailer>> trailers;
    private static MutableLiveData<List<Review>> reviews;

    public CheckMovieViewModel(AppDatabase database, int movieId){
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie(){
        return movie;
    }

    public MutableLiveData<List<Trailer>> getTrailers() {
        if (trailers == null){
            trailers = new MutableLiveData<List<Trailer>>();
        }
        return trailers;
    }

    public static void setTrailers(List<Trailer> trailers) {
        CheckMovieViewModel.trailers.postValue(trailers);
    }


    public MutableLiveData<List<Review>> getReviews() {
        if(reviews == null){
            reviews = new MutableLiveData<List<Review>>();
        }
        return reviews;
    }

    public static void setReviews(List<Review> reviews) {
        CheckMovieViewModel.reviews.postValue(reviews);
    }

}
