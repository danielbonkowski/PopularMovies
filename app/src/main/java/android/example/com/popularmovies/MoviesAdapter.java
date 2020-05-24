package android.example.com.popularmovies;

import android.content.Context;
import android.example.com.popularmovies.data.Movie;
import android.example.com.popularmovies.utilities.NetworkUtils;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movie> mMoviesData;
    private static final String POSTER_SIZE = "w185";

    public void setMoviesData(List<Movie> moviesData){
        Log.v(MoviesAdapter.class.getSimpleName(), "Set movies data");
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    public MoviesAdapter(){

    }



    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(MoviesAdapter.class.getSimpleName(), "On create view holder");
        Context context = parent.getContext();
        int layoutIdForGridItem =  R.layout.grid_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, parent,
                shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        Log.v(MoviesAdapter.class.getSimpleName(), "On bind view holder");
        Movie singleMovieData = mMoviesData.get(position);
        Log.v(MoviesAdapter.class.getSimpleName(), "Movie poster: " + singleMovieData.getMoviePoster());
        Uri imageURL = NetworkUtils.buildPosterUri(POSTER_SIZE, singleMovieData.getMoviePoster());
        Log.v(MoviesAdapter.class.getSimpleName(), imageURL.toString());
        Picasso.get().load(imageURL).into(holder.mMoviesImageView);
    }

    @Override
    public int getItemCount() {
        Log.v(MoviesAdapter.class.getSimpleName(), "On get item count");
        if(mMoviesData == null){
            return 0;
        }
        return mMoviesData.size();
    }



    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMoviesImageView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            Log.v(MoviesAdapterViewHolder.class.getSimpleName(), "Movies adapter view holder constructor");
            mMoviesImageView = (ImageView) itemView.findViewById(R.id.iv_movies_thumbnail);
        }
    }
}
