package android.example.com.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtils {

    public static void loadPosterImage(Context context, String posterId, ImageView imageView){
        String size = calculateImageSize(context);
        Uri imageURL = NetworkUtils.buildPosterUri(size, posterId);
        Picasso.get().load(imageURL).into(imageView);
    }

    private static String calculateImageSize(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        double screenDensity = displayMetrics.densityDpi;
        String size;
        if(screenDensity < 100){
            size = "w94";
        }
        else if(screenDensity < 160){
            size = "w154";
        }else if(screenDensity < 240){
            size = "w185";
        }else if(screenDensity < 320){
            size = "w342";
        }else if(screenDensity < 480){
            size = "w500";
        }else{
            size = "w780";
        }


        return size;
    }

    public static int calculateNrOfColumns(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels;
        float density = displayMetrics.densityDpi;

        String size = calculateImageSize(context);
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(size);
        int imageSizeInPixels = (int)screenWidth;
        if(matcher.find()){
            imageSizeInPixels = Integer.parseInt(matcher.group());
        }

        return Math.round(screenWidth / imageSizeInPixels  * (imageSizeInPixels / density));
    }

    public static int calculateColumnWidth(Context context, int nrOfColumns){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels;

        return (int) Math.ceil(screenWidth / nrOfColumns);
    }
}
