package android.example.com.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private String[] mMoviesData;

    public MoviesAdapter(){

    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        String singleMovieData = mMoviesData[position];
        holder.mMoviesImageView.setImageResource(position);
    }

    @Override
    public int getItemCount() {
        if(mMoviesData == null){
            return 0;
        }
        return mMoviesData.length;
    }



    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMoviesImageView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviesImageView = (ImageView) itemView.findViewById(R.id.iv_movies_thumbnail);
        }
    }
}
