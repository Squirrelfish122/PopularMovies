package com.zhh.hyman.popularmovies;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.zhh.hyman.popularmovies.data.MovieListResult;
import com.zhh.hyman.popularmovies.listener.LoadingDataListener;
import com.zhh.hyman.popularmovies.utils.NetWorkUtils;

import java.net.URL;

/**
 *
 */

public class GetMovieListAsyncTask extends AsyncTask<Void, Void, MovieListResult> {

    public static final int ASYNC_TASK_TYPE_GET_POPULAR_MOVIES = 1;
    public static final int ASYNC_TASK_TYPE_GET_RATED_MOVIES = 2;

    private LoadingDataListener<MovieListResult> loadingDataListener;

    private int mType;

    public GetMovieListAsyncTask(LoadingDataListener<MovieListResult> loadingDataListener, int type) {
        this.loadingDataListener = loadingDataListener;
        this.mType = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (loadingDataListener != null) {
            loadingDataListener.preExecute();
        }
    }

    @Override
    protected MovieListResult doInBackground(Void... voids) {
        URL url = getURL();
        String responseFromHttpUrl = NetWorkUtils.getResponseFromHttpUrl(url);
        if (!TextUtils.isEmpty(responseFromHttpUrl)) {
            return NetWorkUtils.parseData(responseFromHttpUrl, MovieListResult.class);
        }
        return null;
    }

    @Override
    protected void onPostExecute(MovieListResult movieListResult) {
        super.onPostExecute(movieListResult);
        if (loadingDataListener != null) {
            loadingDataListener.postExecute(movieListResult);
        }
    }

    private URL getURL() {
        URL url;
        switch (mType) {
            case ASYNC_TASK_TYPE_GET_RATED_MOVIES:
                url = NetWorkUtils.getRatedURL();
                break;
            case ASYNC_TASK_TYPE_GET_POPULAR_MOVIES:
            default:
                url = NetWorkUtils.getPopularURL();
                break;
        }
        return url;
    }
}
