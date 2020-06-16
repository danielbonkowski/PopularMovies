package android.example.com.popularmovies;

import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class CheckMovieViewModel extends ViewModel {

    private LiveData<Movie> movie;

    public CheckMovieViewModel(AppDatabase database, int movieId){
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie(){
        return movie;
    }
}
