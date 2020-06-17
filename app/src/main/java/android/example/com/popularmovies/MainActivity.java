package android.example.com.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.databinding.ActivityMainBinding;
import android.example.com.popularmovies.utilities.ImageUtils;
import android.example.com.popularmovies.utilities.services.ServicesUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        AdapterView.OnItemSelectedListener{

    private final String TAG = MainActivity.class.getSimpleName();

    private int mSortSpinnerPosition = -1;
    private final int SORT_POPULARITY = 0;
    private final int SORT_TOP_RATED = 1;
    private final int SORT_FAVOURITES = 2;

    private MoviesAdapter mMoviesAdapter;

    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int columnsSpan = ImageUtils.calculateNrOfColumns(MainActivity.this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        GridLayoutManager layoutManager = new GridLayoutManager(this, columnsSpan);

        mBinding.recyclerviewMovies.setLayoutManager(layoutManager);

        mBinding.recyclerviewMovies.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);

        mBinding.recyclerviewMovies.setAdapter(mMoviesAdapter);

        setupViewModel();

    }

    private void setupViewModel(){

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getSpinnerPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer spinnerPosition) {
                setSpinnerPosition(spinnerPosition);
            }
        });

        viewModel.getMostPopularMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                displayMostPopularMoviesIfSelected(movies);
            }
        });

        viewModel.getTopRatedMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                displayTopRatedMoviesIfSelected(movies);
            }
        });

        viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                displayFavoriteMoviesIfSelected(movies);
            }
        });
    }

    private void setSpinnerPosition(Integer spinnerPosition){
        if(spinnerPosition == SORT_POPULARITY){
            mSortSpinnerPosition = SORT_POPULARITY;
        }else if(spinnerPosition == SORT_TOP_RATED){
            mSortSpinnerPosition = SORT_TOP_RATED;
        }else if(spinnerPosition == SORT_FAVOURITES){
            mSortSpinnerPosition = SORT_FAVOURITES;
        }
    }


    private void displayMostPopularMoviesIfSelected(List<Movie> movies){
        if(mSortSpinnerPosition == SORT_POPULARITY) {
            displayMovies(movies);
        }
    }

    private void displayTopRatedMoviesIfSelected(List<Movie> movies){
        if(mSortSpinnerPosition == SORT_TOP_RATED){
            displayMovies(movies);
        }
    }

    private void displayFavoriteMoviesIfSelected(List<Movie> movies){
        if(mSortSpinnerPosition == SORT_FAVOURITES) {
            displayMovies(movies);
        }
    };

    private void displayMovies(List<Movie> movies){
        mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);

        if (movies != null) {
            showMoviesDataView();
            mMoviesAdapter.setMoviesData(movies);
        } else {
            showErrorMessage();
        }
    }



    private void showMoviesDataView() {
        mBinding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mBinding.recyclerviewMovies.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mBinding.recyclerviewMovies.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(Movie movieData) {
        Context context = this;
        Class activityToBeStarted = DetailActivity.class;

        Intent showDetailsIntent = new Intent(context, activityToBeStarted);
        showDetailsIntent.putExtra( "Movie", movieData);

        startActivity(showDetailsIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);

        createMenuSpinner(menu);

        return true;
    }

    private void createMenuSpinner(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_sort);
        Spinner spinner = (Spinner) menuItem.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(mSortSpinnerPosition, true);
        spinner.setOnItemSelectedListener(this);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "Spinner selected at position: " + position );
        if(mSortSpinnerPosition == position){
            return;
        }

        mSortSpinnerPosition = position;

        if(mSortSpinnerPosition == SORT_POPULARITY){
            MainViewModel.setSpinnerPosition(SORT_POPULARITY);
            setMostPopularMovies();
        }else if(mSortSpinnerPosition == SORT_TOP_RATED){
            MainViewModel.setSpinnerPosition(SORT_TOP_RATED);
            setTopRatedMovies();
        }else if(mSortSpinnerPosition == SORT_FAVOURITES){
            MainViewModel.setSpinnerPosition(SORT_FAVOURITES);
            setFavoriteMovies();
        }
        else{
            showErrorMessage();
        }
    }

    private void setMostPopularMovies() {
        mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        ServicesUtils.startMostPopularMoviesService(this);
    }

    private void setTopRatedMovies(){
        mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        ServicesUtils.startTopRatedMoviesService(this);
    }

    private void setFavoriteMovies(){
        mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        recreate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
