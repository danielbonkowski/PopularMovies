package android.example.com.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.MovieDatabaseJsonUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, AdapterView.OnItemSelectedListener{

    private final int GRID_NR_OR_COLUMNS = 3;
    private final String SORT_POPULARITY = "0";
    private final String SORT_TOP_RATED = "1";

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private int mSortSpinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            Log.i(MainActivity.class.getSimpleName(), "Instance is empty");
            fetchMostPopularMovies();

        }else{
            Log.i(MainActivity.class.getSimpleName(), "Instance is not empty");
            List<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
            mMoviesAdapter.setMoviesData(movies);
            mRecyclerView.setAdapter(mMoviesAdapter);

            mSortSpinnerPosition = savedInstanceState.getInt("spinnerPosition");
            Log.i(MainActivity.class.getSimpleName(), "Spinner instance position: " + mSortSpinnerPosition);
        }

    }

    private void fetchMostPopularMovies() {
        Log.i(MainActivity.class.getSimpleName(), "Fetch most popular movies");
        showMoviesDataView();

        new FetchMoviesTask().execute(SORT_POPULARITY);
    }

    private void fetchTopRatedMovies(){
        Log.i(MainActivity.class.getSimpleName(), "Fetch top rated movies");
        showMoviesDataView();

        new FetchMoviesTask().execute(SORT_TOP_RATED);
    }

    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(MainActivity.class.getSimpleName(), "onSaveInstanceState");
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
        showDetailsIntent.putExtra( "Movie", (Parcelable) movieData);

        startActivityForResult(showDetailsIntent, 0);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(MainActivity.class.getSimpleName(), "On create options menu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);

        // Creates a spinner and specifies choices
        MenuItem menuItem = menu.findItem(R.id.action_sort);
        Spinner spinner = (Spinner) menuItem.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(mSortSpinnerPosition, true);
        spinner.setOnItemSelectedListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.v(MainActivity.class.getSimpleName(), "On options item selected");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.v(MainActivity.class.getSimpleName(), "Position: " + position);

        if(mSortSpinnerPosition == position){
            return;
        }

        mSortSpinnerPosition = position;

        if(mSortSpinnerPosition == Integer.valueOf(SORT_POPULARITY)){
            fetchMostPopularMovies();
        }else if(mSortSpinnerPosition == Integer.valueOf(SORT_TOP_RATED)){
            fetchTopRatedMovies();
        }else{
            showErrorMessage();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            if(params.length == 0){
                return null;
            }

            String sortType = params[0];
            URL moviesRequestUrl = sortType == SORT_TOP_RATED ?
                    NetworkUtils.buildTopRatedUrl() : NetworkUtils.buildPopularityUrl();

            try{
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                List<Movie> movieJsonData = MovieDatabaseJsonUtils.
                        getMovieObjectsFromJson(jsonMoviesResponse);

                return movieJsonData;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Movie> moviesData) {
            super.onPostExecute(moviesData);
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if(moviesData != null){
                showMoviesDataView();
                mMoviesAdapter.setMoviesData(moviesData);
            }else{
                showErrorMessage();
            }
        }
    }
}
