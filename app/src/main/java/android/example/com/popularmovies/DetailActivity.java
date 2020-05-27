package android.example.com.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.ImageUtils;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

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


        mPosterImageView = findViewById(R.id.iv_details_poster);
        mOriginalTitleTextView = findViewById(R.id.tv_details_original_title);
        mUserRatingTextView = findViewById(R.id.tv_details_user_rating);
        mSynopsisTextView = findViewById(R.id.tv_details_synopsis);
        mReleaseDateTextView = findViewById(R.id.tv_details_release_date);

        showDetails(movie);
    }

    private void showDetails(Movie movie){

        if(movie == null){
            return;
        }

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
