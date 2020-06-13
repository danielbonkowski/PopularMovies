package android.example.com.popularmovies;

import android.content.Context;
import android.example.com.popularmovies.data.Trailer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private List<Trailer> mTrailersData = null;

    private final TrailersAdapterOnClickHandler mClickHandler;

    public TrailersAdapter(TrailersAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public interface TrailersAdapterOnClickHandler{
        void onClick(Trailer trailer);
    }

    public ArrayList<Trailer> getTrailersData(){
        if(mTrailersData == null){
            return null;
        }
        return new ArrayList<>(mTrailersData);
    }

    public void setTrailersData(List<Trailer> trailersData){
        if(trailersData != null){
            mTrailersData = new ArrayList<>(trailersData);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int trailersListItemId = R.layout.trailers_list_item;
        boolean shouldAttachToParentImmediately = false;

        View view = LayoutInflater.from(context).inflate(trailersListItemId, parent,
                shouldAttachToParentImmediately);

        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapterViewHolder holder, int position) {
            Trailer singleTrailerData =  mTrailersData.get(position);

            holder.mTrailerNameTextView.setText(singleTrailerData.getName());
    }

    @Override
    public int getItemCount() {
        if(mTrailersData == null){
            return 0;
        }
        return mTrailersData.size();
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTrailerNameTextView;


        public TrailersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mTrailerNameTextView = itemView.findViewById(R.id.tv_trailer_name);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer trailerData = mTrailersData.get(adapterPosition);
            mClickHandler.onClick(trailerData);
        }
    }
}
