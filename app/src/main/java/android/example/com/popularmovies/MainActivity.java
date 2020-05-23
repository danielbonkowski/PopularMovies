package android.example.com.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

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


        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_NR_OR_COLUMNS,
                RecyclerView.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            if(params.length == 0){
                return null;
            }

            String sortType = params[0];
            URL moviesRequestUrl = sortType == SORT_TOP_RATED ?
                    NetworkUtils.buildTopRatedUrl() : NetworkUtils.buildPopularityUrl();

            try{
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                Movie[] movieJsonWeatherData = null;

                return movieJsonWeatherData;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] moviesData) {
            super.onPostExecute(moviesData);
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if(moviesData != null){
                // To Do set adapter data
            }else{
                // To do show error message
            }
        }
    }
}
