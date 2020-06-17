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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.example.com.popularmovies.data.AppDatabase;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.data.Review;
import android.example.com.popularmovies.data.Trailer;
import android.example.com.popularmovies.databinding.ActivityDetailBinding;
import android.example.com.popularmovies.utilities.ImageUtils;
import android.example.com.popularmovies.utilities.MovieDatabaseJsonUtils;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.example.com.popularmovies.utilities.services.ServicesUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements
        TrailersAdapter.TrailersAdapterOnClickHandler{

    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final int TRAILERS_LOADER_ID = 10;
    private static final int REVIEWS_LOADER_ID = 20;

    private static final String SEARCH_QUERY_EXTRA = "id_query";

    Movie movie = null;
    boolean isFavourite = true;

    private ActivityDetailBinding mBinding;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private AppDatabase mDb;

    private LoaderManager.LoaderCallbacks<List<Trailer>> trailersResultsLoaderListener =
            new LoaderManager.LoaderCallbacks<List<Trailer>>() {
                @NonNull
                @Override
                public Loader<List<Trailer>> onCreateLoader(int id, @Nullable final Bundle bundle) {
                    return new AsyncTaskLoader<List<Trailer>>(getApplicationContext()) {

                        @Override
                        protected void onStartLoading() {
                            super.onStartLoading();

                            if(bundle == null){
                                return;
                            }

                            mBinding.pbTrailers.setVisibility(View.VISIBLE);

                            forceLoad();
                        }

                        @Nullable
                        @Override
                        public List<Trailer> loadInBackground() {

                            String trailersQueryUrlString = bundle.getString(SEARCH_QUERY_EXTRA);

                            if(trailersQueryUrlString == null || trailersQueryUrlString.isEmpty()){
                                return null;
                            }

                            String jsonTrailersResults = null;
                            try{
                                URL trailersUrl = new URL(trailersQueryUrlString);

                                Log.v(TAG, "My URL: " + trailersUrl.toString());

                                jsonTrailersResults = NetworkUtils.getResponseFromHttpUrl(trailersUrl);

                                Log.v(TAG, "My JSON: " + jsonTrailersResults);

                                return MovieDatabaseJsonUtils.getTrailerObjectsFromJson(jsonTrailersResults);

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                return null;
                            }

                        }
                    };
                }

                @Override
                public void onLoadFinished(@NonNull Loader<List<Trailer>> loader, List<Trailer> data) {
                    mBinding.pbTrailers.setVisibility(View.INVISIBLE);

                    if(data != null){
                        showTrailersDataView();
                        mTrailersAdapter.setTrailersData(data);
                    }else{
                        showTrailersErrorMessage();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<List<Trailer>> loader) {

                }
            };

    private LoaderManager.LoaderCallbacks<List<Review>> reviewsResultsLoaderListener =
            new LoaderManager.LoaderCallbacks<List<Review>>() {
                @NonNull
                @Override
                public Loader<List<Review>> onCreateLoader(int id, @Nullable final Bundle bundle) {
                    return new AsyncTaskLoader<List<Review>>(getApplicationContext()) {

                        @Override
                        protected void onStartLoading() {

                            if (bundle == null){
                                return;
                            }

                            mBinding.pbReviews.setVisibility(View.VISIBLE);

                            forceLoad();
                        }

                        @Nullable
                        @Override
                        public List<Review> loadInBackground() {

                            String reviewsQueryUrlString = bundle.getString(SEARCH_QUERY_EXTRA);

                            if(reviewsQueryUrlString == null || reviewsQueryUrlString.isEmpty()){
                                return null;
                            }

                            String jsonReviewsResults = null;
                            try{
                                URL reviewsUrl = new URL(reviewsQueryUrlString);

                                jsonReviewsResults = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);


                                return MovieDatabaseJsonUtils.getReviewObjectsFromJson(jsonReviewsResults);

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    };
                }

                @Override
                public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> data) {
                    mBinding.pbReviews.setVisibility(View.INVISIBLE);

                    if(data != null){
                        showReviewsDataView();
                        mReviewsAdapter.setReviewsData(data);
                    }else{
                        showReviewsErrorMessage();
                    }


                }

                @Override
                public void onLoaderReset(@NonNull Loader<List<Review>> loader) {

                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parcelable parcelableMovie =  getIntent().getParcelableExtra("Movie");
        movie = (Movie) parcelableMovie;

        mDb = AppDatabase.getInstance(getApplicationContext());
        setupViewModel();

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


        loadAllDetails(movie);
        
        setupViewModel();

    }


    private void loadAllDetails(Movie movie){
        String movieId = String.valueOf(movie.getMovieId());

        ServicesUtils.startTrailersService(this, movieId);
        loadReviews(movieId);
        showDetails(movie);
    }

    private void setupViewModel() {

        CheckMovieViewModelFactory factory = new CheckMovieViewModelFactory(mDb, movie.getMovieId());
        final CheckMovieViewModel viewModel = ViewModelProviders.of(this, factory).get(CheckMovieViewModel.class);
        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                viewModel.getMovie().removeObserver(this);
                Log.d(TAG, "Receiving database update from LiveData");
                if (movie == null){
                    displayNotFavoriteIcon();
                }else{
                    displayFavoriteIcon();
                }
            }
        });

        viewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                if(trailers != null){
                    showTrailersDataView();
                    mTrailersAdapter.setTrailersData(trailers);
                }else{
                    showTrailersErrorMessage();
                }

            }
        });
        
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

    private void loadTrailers(String filmId){

        URL trailersSearchUrl = NetworkUtils.buildTrailersUrl(filmId);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_EXTRA, trailersSearchUrl.toString());

        LoaderManager loaderManager =  getSupportLoaderManager();
        Loader<List<Trailer>> loader = loaderManager.getLoader(TRAILERS_LOADER_ID);

        if(loader == null){
            loaderManager.initLoader(TRAILERS_LOADER_ID, queryBundle, trailersResultsLoaderListener);
        }else{
            loaderManager.restartLoader(TRAILERS_LOADER_ID, queryBundle, trailersResultsLoaderListener);
        }
    }


    private void loadReviews(String filmId){

        URL reviewsSearchUrl = NetworkUtils.buildReviewsUrl(filmId);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_EXTRA, reviewsSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Review>> loader = loaderManager.getLoader(REVIEWS_LOADER_ID);

        if(loader == null){
            loaderManager.initLoader(REVIEWS_LOADER_ID, queryBundle, reviewsResultsLoaderListener);
        }else {
            loaderManager.restartLoader(REVIEWS_LOADER_ID, queryBundle, reviewsResultsLoaderListener);
        }

    }

    @Override
    public void onClick(Trailer trailer) {
        Log.v("HERE", "I clicked it");
        Context context = this;
        Uri youtubeUrl = NetworkUtils.buildYoutubeTrailerUrl(trailer.getKey());
        Intent showTrailerIntent = new Intent(Intent.ACTION_VIEW, youtubeUrl);

        if(showTrailerIntent.resolveActivity(getPackageManager()) != null){
            startActivity(showTrailerIntent);
        }
    }



    public void starClick(View view) {

        if(isFavourite){
            isFavourite = false;
            removeFromFavorites();
        }else{
            isFavourite = true;
            addToFavorites();
        }
    }

    private void addToFavorites() {
        displayFavoriteIcon();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(movie);
            }
        });
    }

    private void removeFromFavorites() {
        displayNotFavoriteIcon();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovie(movie);
            }
        });
    }

    private void displayNotFavoriteIcon(){
        mBinding.ivFavouriteStar.setImageResource(android.R.drawable.btn_star_big_off);
    }

    private void displayFavoriteIcon(){
        mBinding.ivFavouriteStar.setImageResource(android.R.drawable.btn_star_big_on);
    }
}
