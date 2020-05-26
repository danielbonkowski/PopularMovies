package android.example.com.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.ImageUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private final String POSTER_SIZE = "w185";

    private ImageView mPosterImageView;
    private TextView mOriginalTitleTextView;
    private TextView mUserRatingTextView;
    private TextView mSynopsisTextView;
    private TextView mReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Parcelable parcelableMovie =  getIntent().getParcelableExtra("Movie");
        Movie movie = (Movie) parcelableMovie;


        mPosterImageView = (ImageView) findViewById(R.id.iv_details_poster);
        mOriginalTitleTextView = (TextView) findViewById(R.id.tv_details_original_title);
        mUserRatingTextView = (TextView) findViewById(R.id.tv_details_user_rating);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_details_synopsis);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_details_release_date);

        showDetails(movie);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("returnMovies",
                getIntent().getParcelableArrayListExtra("movies"));
        setResult(1, returnIntent);
        finish();
    }

    public void showDetails(Movie movie){
        Context context = mPosterImageView.getContext();
        ImageUtils.loadPosterImage(context, movie.getMoviePoster(),
                mPosterImageView);

        String originalTitle = movie.getOriginalTitle();
        Double userRating =  movie.getUserAverageRating();
        String plotSynopsis = movie.getPlotSynopsis();
        String releaseDate = movie.getReleaseDate(DetailActivity.this);


        mOriginalTitleTextView.setText(originalTitle);
        mUserRatingTextView.setText(userRating.toString());
        mSynopsisTextView.setText(plotSynopsis);
        mReleaseDateTextView.setText(releaseDate);
    }
}
