package android.example.com.popularmovies.data.model;

import android.example.com.popularmovies.data.AppDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mMovieId;

    public DetailsViewModelFactory(AppDatabase mDb, int mMovieId) {
        this.mDb = mDb;
        this.mMovieId = mMovieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(mDb, mMovieId);
    }
}
