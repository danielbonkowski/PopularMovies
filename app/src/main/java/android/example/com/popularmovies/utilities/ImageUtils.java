package android.example.com.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtils {

    public static void loadPosterImage(String size, String posterId, ImageView imageView){
        Uri imageURL = NetworkUtils.buildPosterUri(size, posterId);
        Picasso.get().load(imageURL).into(imageView);
    }

    
}
