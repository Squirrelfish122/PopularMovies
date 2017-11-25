package com.zhh.hyman.popularmovies.utils;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zhh.hyman.popularmovies.R;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */

public class NetWorkUtils {

    private static final String TAG = NetWorkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String POPULAR_URL = BASE_URL + "/popular";
    private static final String RATED_URL = BASE_URL + "/top_rated";

    private static final String PARAM_API_KEY = "api_key";

    private static String DIMENSION = "w185/";
    private static final int RESPONSE_CODE_SUCCESS = 200;
    private static final int READ_TIME_OUT = 10 * 1000;
    private static final int CONNECT_TIME_OUT = 10 * 1000;

    // TODO: 2017/10/12 update to your own api key
    private static final String API_KEY = "abcdefgh";

    private static String getMovieDetailUrl(String movieId) {
        return BASE_URL + "/" + movieId;
    }

    private static URL buildURL(String host) {
        Log.i(TAG, "buildURL , host = " + host);
        Uri build = Uri.parse(host).buildUpon().
                appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        try {
            return new URL(build.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL getPopularURL() {
        return buildURL(POPULAR_URL);
    }

    public static URL getRatedURL() {
        return buildURL(RATED_URL);
    }

    public static URL getDetailURL(String movieId) {
        return buildURL(getMovieDetailUrl(movieId));
    }

    public static String getResponseFromHttpUrl(URL url) {
        if (url == null) {
            return null;
        }

        Log.i(TAG,"getResponseFromHttpUrl , url = " + url.toString());
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpURLConnection.setReadTimeout(READ_TIME_OUT);
            int responseCode = httpURLConnection.getResponseCode();
            Log.i(TAG, "responseCode = " + responseCode);
            if (responseCode == RESPONSE_CODE_SUCCESS) {
                InputStream inputStream = httpURLConnection.getInputStream();
                return getStringFromInputStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

    private static Gson gson;

    private static Gson getGson() {
        if (gson == null) {
            synchronized (NetWorkUtils.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    public static <T> T parseData(String data, Class<T> tClass) {
        return getGson().fromJson(data, tClass);
    }

    public static void loadImage(ImageView imageView, String relativePath) {
        String imageUrl = getImageUrl(relativePath);
        Log.i(TAG, "imageUrl = " + imageUrl);
        Picasso.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.load_fail)
                .into(imageView);
    }

    private static String getImageUrl(String relativePath) {
        return BASE_IMAGE_URL + DIMENSION + relativePath;
    }

    private static String getStringFromInputStream(InputStream inputStream) {
        if (inputStream != null) {
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                quietClose(bufferedReader);
                quietClose(inputStreamReader);
                quietClose(inputStream);
            }
        }
        return null;
    }

    private static void quietClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
