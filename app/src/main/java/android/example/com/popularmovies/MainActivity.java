package android.example.com.popularmovies;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private final int GRID_NR_OR_COLUMNS = 3;
    private final String SORT_POPULARITY = "most_popular";
    private final String SORT_TOP_RATED = "top_rated";

    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

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

        loadMoviesData();
    }

    private void loadMoviesData() {
        showMoviesDataView();

        new FetchMoviesTask().execute(SORT_POPULARITY);
    }

    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadImage(Movie movie, ImageView posterView){
        mLoadingIndicator.setVisibility(View.VISIBLE);


    }

    @Override
    public void onClick(Movie movieData) {
        Context context = this;
        Class activityToBeStarted = DetailActivity.class;

        Intent showDetailsIntent = new Intent(context, activityToBeStarted);
        showDetailsIntent.putExtra( "Movie",movieData);

        startActivity(showDetailsIntent);


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
