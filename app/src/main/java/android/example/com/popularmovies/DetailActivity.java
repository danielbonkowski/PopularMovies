package android.example.com.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.databinding.ActivityDetailBinding;
import android.example.com.popularmovies.utilities.ImageUtils;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding mBinding;
    private TrailersAdapter mTrailersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parcelable parcelableMovie =  getIntent().getParcelableExtra("Movie");
        Movie movie = (Movie) parcelableMovie;

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        LinearLayoutManager layoutManager =new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false);

        mBinding.rvTrailers.setLayoutManager(layoutManager);

        mBinding.rvTrailers.setHasFixedSize(true);

        mTrailersAdapter = new TrailersAdapter();



        showDetails(movie);
    }

    private void showDetails(Movie movie){

        if(movie == null){
            return;
        }

        Context context = mBinding.ivDetailsPoster.getContext();
        ImageUtils.loadPosterImage(context, movie.getMoviePoster(),
                mBinding.ivDetailsPoster);

        String originalTitle = movie.getOriginalTitle();
        Double userRating =  movie.getUserAverageRating();
        String plotSynopsis = movie.getPlotSynopsis();
        String releaseDate = movie.getReleaseDate(DetailActivity.this);


        mBinding.tvDetailsOriginalTitle.setText(originalTitle);
        mBinding.tvDetailsUserRating.setText(userRating.toString());
        mBinding.tvDetailsSynopsis.setText(plotSynopsis);
        mBinding.tvDetailsReleaseDate.setText(releaseDate);
    }
}
