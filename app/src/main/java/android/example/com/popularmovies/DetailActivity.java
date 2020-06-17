package android.example.com.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.data.Review;
import android.example.com.popularmovies.data.Trailer;
import android.example.com.popularmovies.data.model.DetailsViewModel;
import android.example.com.popularmovies.data.model.DetailsViewModelFactory;
import android.example.com.popularmovies.databinding.ActivityDetailBinding;
import android.example.com.popularmovies.utilities.ImageUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.example.com.popularmovies.utilities.services.ServicesUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements
        TrailersAdapter.TrailersAdapterOnClickHandler{

    private static final String SEARCH_QUERY_EXTRA = "id_query";

    Movie movie = null;
    boolean isFavourite = true;

    private ActivityDetailBinding mBinding;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parcelable parcelableMovie =  getIntent().getParcelableExtra("Movie");
        movie = (Movie) parcelableMovie;

        mDb = AppDatabase.getInstance(getApplicationContext());

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        LinearLayoutManager trailersLayoutManager =new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false);
        mBinding.rvTrailers.setLayoutManager(trailersLayoutManager);
        mBinding.rvTrailers.setHasFixedSize(true);
        mTrailersAdapter = new TrailersAdapter(this);
        mBinding.rvTrailers.setAdapter(mTrailersAdapter);

        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false);
        mBinding.rvReviews.setLayoutManager(reviewsLayoutManager);
        mBinding.rvReviews.setHasFixedSize(true);
        mReviewsAdapter = new ReviewsAdapter();
        mBinding.rvReviews.setAdapter(mReviewsAdapter);


        setupViewModel();
        loadAllDetails(movie);
    }


    private void loadAllDetails(Movie movie){
        String movieId = String.valueOf(movie.getMovieId());

        ServicesUtils.startTrailersService(this, movieId);
        ServicesUtils.startReviewsService(this, movieId);
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

    private void setupViewModel() {

        DetailsViewModelFactory factory = new DetailsViewModelFactory(mDb, movie.getMovieId());
        final DetailsViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel.class);
        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                viewModel.getMovie().removeObserver(this);
                displayRightIcon(movie);
            }
        });

        viewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                displayTrailers(trailers);
            }
        });

        viewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                displayReviews(reviews);
            }
        });

    }

    private void displayTrailers(List<Trailer> trailers){
        if(trailers != null && trailers.size() > 0){
            showTrailersDataView();
            mTrailersAdapter.setTrailersData(trailers);
        }else{
            showTrailersErrorMessage();
        }
    }

    private void displayReviews(List<Review> reviews){
        if(reviews != null && reviews.size() > 0){
            showReviewsDataView();
            mReviewsAdapter.setReviewsData(reviews);
        }else {
            showReviewsErrorMessage();
        }
    }

    private void displayRightIcon(Movie movie){
        if (movie == null){
            displayNotFavoriteIcon();
        }else{
            displayFavoriteIcon();
        }
    }

    private void showTrailersDataView(){
        mBinding.rvTrailers.setVisibility(View.VISIBLE);
        mBinding.tvErrorTrailers.setVisibility(View.INVISIBLE);
    }

    private void showTrailersErrorMessage(){
        mBinding.tvErrorTrailers.setVisibility(View.VISIBLE);
        mBinding.rvTrailers.setVisibility(View.INVISIBLE);
    }

    private void showReviewsDataView(){
        mBinding.rvReviews.setVisibility(View.VISIBLE);
        mBinding.tvErrorReviews.setVisibility(View.INVISIBLE);
    }

    private void showReviewsErrorMessage(){
        mBinding.tvErrorReviews.setVisibility(View.VISIBLE);
        mBinding.rvReviews.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onTrailerClick(Trailer trailer) {
        Uri youtubeUrl = NetworkUtils.buildYoutubeTrailerUrl(trailer.getKey());
        Intent showTrailerIntent = new Intent(Intent.ACTION_VIEW, youtubeUrl);

        if(showTrailerIntent.resolveActivity(getPackageManager()) != null){
            startActivity(showTrailerIntent);
        }
    }


    public void onStarClick(View view) {
        if(isFavourite){
            removeFromFavorites();
        }else{
            addToFavorites();
        }
    }

    private void addToFavorites() {
        addMovieToDb();
        displayFavoriteIcon();
    }

    private void removeFromFavorites() {
        removeMovieFromDb();
        displayNotFavoriteIcon();
    }

    private void addMovieToDb(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(movie);
            }
        });
    }

    private void removeMovieFromDb(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovie(movie);
            }
        });
    }

    private void displayNotFavoriteIcon(){
        isFavourite = false;
        mBinding.ivFavouriteStar.setImageResource(android.R.drawable.btn_star_big_off);
    }

    private void displayFavoriteIcon(){
        isFavourite = true;
        mBinding.ivFavouriteStar.setImageResource(android.R.drawable.btn_star_big_on);

    }
}
