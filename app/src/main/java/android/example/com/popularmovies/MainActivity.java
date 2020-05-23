package android.example.com.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.com.popularmovies.data.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private final int GRID_NR_OR_COLUMNS = 3;

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
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            return new Movie[0];
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
        }
    }
}
