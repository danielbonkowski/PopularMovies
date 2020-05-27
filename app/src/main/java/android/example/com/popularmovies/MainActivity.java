package android.example.com.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.ImageUtils;
import android.example.com.popularmovies.utilities.MovieDatabaseJsonUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, AdapterView.OnItemSelectedListener{

    private final int SORT_POPULARITY = 0;
    private final int SORT_TOP_RATED = 1;

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private int mSortSpinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int columnsSpan = ImageUtils.calculateNrOfColumns(MainActivity.this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        GridLayoutManager layoutManager = new GridLayoutManager(this, columnsSpan);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            fetchMostPopularMovies();

        }else{
            List<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
            if(movies == null){
                showErrorMessage();
                return;
            }
            mMoviesAdapter.setMoviesData(movies);
            mRecyclerView.setAdapter(mMoviesAdapter);

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
        }else{
            showErrorMessage();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class FetchMoviesTask extends AsyncTask<Integer, Void, List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
