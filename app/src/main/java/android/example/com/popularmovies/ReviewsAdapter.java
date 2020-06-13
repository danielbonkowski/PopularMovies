package android.example.com.popularmovies;

import android.content.Context;
import android.example.com.popularmovies.data.Review;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private List<Review> mReviewsData = null;

    private final int MAX_REVIEW_LINES =  Integer.MAX_VALUE;
    private final int FEW_REVIEW_LINES = 3;

    public List<Review> getReviewsData(){
        if(mReviewsData == null){
            return null;
        }
        return new ArrayList<>(mReviewsData);
    }

    public void setReviewsData(List<Review> reviewsData){
        if(reviewsData != null){
            mReviewsData = new ArrayList<>(reviewsData);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ReviewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int reviewsListItemId = R.layout.reviews_list_item;
        boolean shouldAttachToParentImmediately = false;

        View view = LayoutInflater.from(context).inflate(reviewsListItemId, parent,
                shouldAttachToParentImmediately);

        return new ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapterViewHolder holder, int position) {
        Review singleReviewData = mReviewsData.get(position);

        holder.mReviewAuthor.setText(singleReviewData.getAuthor());
        holder.mReviewContent.setText(singleReviewData.getContent());
    }

    @Override
    public int getItemCount() {
        if(mReviewsData == null){
            return 0;
        }
        return mReviewsData.size();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView mReviewAuthor;
        TextView mReviewContent;

        public ReviewsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mReviewAuthor = itemView.findViewById(R.id.tv_review_author);
            mReviewContent = itemView.findViewById(R.id.tv_review_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Review review = mReviewsData.get(adapterPosition);
            TextUtils.TruncateAt elipsizeType = mReviewContent.getEllipsize();

            boolean isElipsized = false;
            if(elipsizeType == TextUtils.TruncateAt.END){
                isElipsized = true;
            }

            if(isElipsized){
                mReviewContent.setEllipsize(null);
                mReviewContent.setMaxLines(MAX_REVIEW_LINES);
            }else{
                mReviewContent.setEllipsize(TextUtils.TruncateAt.END);
                mReviewContent.setMaxLines(FEW_REVIEW_LINES);
            }

            mReviewContent.setText(review.getContent());
        }
    }
}
