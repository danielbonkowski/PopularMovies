package android.example.com.popularmovies.data.model;

import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.data.Review;
import android.example.com.popularmovies.data.Trailer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    private static LiveData<Movie> movie;
    private static MutableLiveData<List<Trailer>> trailers;
    private static MutableLiveData<List<Review>> reviews;

    public DetailsViewModel(AppDatabase database, int movieId){
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie(){
        return movie;
    }

    public MutableLiveData<List<Trailer>> getTrailers() {
        if (trailers == null){
            trailers = new MutableLiveData<>();
        }
        return trailers;
    }

    public static void setTrailers(List<Trailer> trailers) {
        DetailsViewModel.trailers.postValue(trailers);
    }


    public MutableLiveData<List<Review>> getReviews() {
        if(reviews == null){
            reviews = new MutableLiveData<>();
        }
        return reviews;
    }

    public static void setReviews(List<Review> reviews) {
        DetailsViewModel.reviews.postValue(reviews);
    }

}
