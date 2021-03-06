package android.example.com.popularmovies.data.model;

import android.app.Application;
import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private static MutableLiveData<Integer> spinnerPosition;
    private static LiveData<List<Movie>> favoriteMovies;
    private static MutableLiveData<List<Movie>> mostPopularMovies;
    private static MutableLiveData<List<Movie>> topRatedMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(getApplication());
        favoriteMovies = database.movieDao().loadAllMovies();
    }

    public MutableLiveData<Integer> getSpinnerPosition() {
        if(spinnerPosition == null){
            spinnerPosition = new MutableLiveData<>();
        }
        return spinnerPosition;
    }

    public static void setSpinnerPosition(Integer spinnerPosition) {
        MainViewModel.spinnerPosition.postValue(spinnerPosition);
    }


    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        if(topRatedMovies == null){
            topRatedMovies = new MutableLiveData<>();
        }
        return topRatedMovies;
    }

    public static void setTopRatedMovies(List<Movie> topRatedMovies) {
        MainViewModel.topRatedMovies.postValue(topRatedMovies);
    }


    public LiveData<List<Movie>> getMostPopularMovies() {
        if(mostPopularMovies == null){
            MainViewModel.mostPopularMovies = new MutableLiveData<>();
        }
        return MainViewModel.mostPopularMovies;
    }

    public static void setMostPopularMovies(List<Movie> mostPopularMovies) {
        MainViewModel.mostPopularMovies.postValue(mostPopularMovies);
    }
}
