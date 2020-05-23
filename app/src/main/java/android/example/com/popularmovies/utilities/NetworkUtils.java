package android.example.com.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String GRID_MOVIES_URL =
            "http://api.themoviedb.org/3/discover/movie?";

    private static final String THUMBNAIL_URL =
            " http://image.tmdb.org/t/p/";


    private static final String api_key =
            "58805f67e972e203a3e4cba8d3e54e00";
    private static final String sort_popularity = "popularity.desc";
    private static final String sort_top_rated = "vote_average.desc";


    final static String SORT_PARAM = "sort_by";
    final static String API_KEY_PARAM = "api_key";


    public static URL buildPopularityUrl(){
        return buildSortUrl(sort_popularity);
    }


    public static URL buildTopRatedUrl(){
        return buildSortUrl(sort_top_rated);
    }


    public static URL buildSortUrl(String sortType){

        Uri builtUri = Uri.parse(GRID_MOVIES_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, sortType)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();

        return buildUrl(builtUri);
    }


    public static URL buildThumbnailUrl(String thumbSize, String thumbPath){

        Uri builtUri = Uri.parse(THUMBNAIL_URL).buildUpon()
                .appendPath(thumbSize)
                .appendPath(thumbSize)
                .build();

        return buildUrl(builtUri);
    }


    public static URL buildUrl(Uri builtUri){

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
