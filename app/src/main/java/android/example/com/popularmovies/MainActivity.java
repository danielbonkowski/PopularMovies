package android.example.com.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.databinding.ActivityMainBinding;
import android.example.com.popularmovies.utilities.ImageUtils;
import android.example.com.popularmovies.utilities.MovieDatabaseJsonUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler,
        AdapterView.OnItemSelectedListener,
        LoaderManager.LoaderCallbacks<String>{

    private final String TAG = MainActivity.class.getSimpleName();

    private final int SORT_POPULARITY = 0;
    private final int SORT_TOP_RATED = 1;
    private final int SORT_FAVOURITES = 2;

    private MoviesAdapter mMoviesAdapter;

    private int mSortSpinnerPosition = 0;

    private ActivityMainBinding mBinding;

    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabase.getInstance(getApplicationContext());
        setupFavoritesViewModel();

        int columnsSpan = ImageUtils.calculateNrOfColumns(MainActivity.this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        GridLayoutManager layoutManager = new GridLayoutManager(this, columnsSpan);

        mBinding.recyclerviewMovies.setLayoutManager(layoutManager);

        mBinding.recyclerviewMovies.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);

        mBinding.recyclerviewMovies.setAdapter(mMoviesAdapter);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            fetchMostPopularMovies();

        }else{
            List<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
            if(movies == null){
                showErrorMessage();
                return;
            }
            mMoviesAdapter.setMoviesData(movies);
            mBinding.recyclerviewMovies.setAdapter(mMoviesAdapter);

            mSortSpinnerPosition = savedInstanceState.getInt("spinnerPosition");
        }

    }

    private void fetchMostPopularMovies() {
        showMoviesDataView();

        new FetchMoviesTask().execute(SORT_POPULARITY);
    }

    private void fetchTopRatedMovies(){
        showMoviesDataView();

        new FetchMoviesTask().execute(SORT_TOP_RATED);
    }

    private void setupFavoritesViewModel(){

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Log.d(TAG, "Updating list of movies from LiveData in ViewModel");
                mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);

                if(movies != null){
                    showMoviesDataView();
                    mMoviesAdapter.setMoviesData(movies);
                }else{
                    showErrorMessage();
                }
            }
        });
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(mMoviesAdapter != null){
            outState.putParcelableArrayList("movies", mMoviesAdapter.getMoviesData());
            outState.putInt("spinnerPosition", mSortSpinnerPosition);
        }

        super.onSaveInstanceState(outState);
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
        if(mSortSpinnerPosition == position){
            return;
        }

        mSortSpinnerPosition = position;

        if(mSortSpinnerPosition == SORT_POPULARITY){
            fetchMostPopularMovies();
        }else if(mSortSpinnerPosition == SORT_TOP_RATED){
            fetchTopRatedMovies();
        }else if(mSortSpinnerPosition == SORT_FAVOURITES){

        }
        else{
            showErrorMessage();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String>(this){
            @Nullable
            @Override
            public String loadInBackground() {
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    class FetchMoviesTask extends AsyncTask<Integer, Void, List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Integer... params) {



            if(params.length == 0){
                return null;
            }

            int sortType = params[0];
            URL moviesRequestUrl = sortType == SORT_TOP_RATED ?
                    NetworkUtils.buildTopRatedUrl() : NetworkUtils.buildPopularityUrl();

            try{
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                return MovieDatabaseJsonUtils.getMovieObjectsFromJson(jsonMoviesResponse);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "Error: " + e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Movie> moviesData) {
            super.onPostExecute(moviesData);
            mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);


            if(moviesData != null){
                showMoviesDataView();
                mMoviesAdapter.setMoviesData(moviesData);
            }else{
                showErrorMessage();
            }
        }
    }
}
