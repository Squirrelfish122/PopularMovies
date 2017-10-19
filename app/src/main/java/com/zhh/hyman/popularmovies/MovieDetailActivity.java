package com.zhh.hyman.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhh.hyman.popularmovies.data.MovieDetail;
import com.zhh.hyman.popularmovies.utils.NetWorkUtils;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String PARAM_KEY_MOVIE_ID = "param_key_movie_id";

    private long INVALID_MOVIE_ID = -1;
    private long movieId = INVALID_MOVIE_ID;

    private RelativeLayout contentLayout;

    private ImageView poster;

    private TextView title;
    private TextView release;
    private TextView rate;
    private TextView overview;

    private Button retryButton;
    private ProgressBar pbIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initView();

        Intent intent = getIntent();
        if (intent.hasExtra(PARAM_KEY_MOVIE_ID)) {
            movieId = intent.getLongExtra(PARAM_KEY_MOVIE_ID, -1);
        }
        Log.i(TAG,"movieId = "+ movieId);

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getMovieDetailTask != null){
            getMovieDetailTask.cancel(false);
        }
    }

    private void initView() {
        contentLayout = (RelativeLayout) findViewById(R.id.movie_detail_content_layout);
        poster = (ImageView) findViewById(R.id.movie_detail_poster);
        title = (TextView) findViewById(R.id.movie_detail_title);
        release = (TextView) findViewById(R.id.movie_detail_release_date);
        rate = (TextView) findViewById(R.id.movie_detail_rate);
        overview = (TextView) findViewById(R.id.movie_detail_overview);

        retryButton = (Button) findViewById(R.id.movie_detail_btn_retry);
        pbIndicator = (ProgressBar) findViewById(R.id.movie_detail_pb_indicator);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }

    private GetMovieDetailTask getMovieDetailTask;

    private void loadData() {
        if (getMovieDetailTask != null) {
            getMovieDetailTask.cancel(false);
        }

        if (movieId == INVALID_MOVIE_ID) {
            Log.i(TAG, " movie id is invalid");
            showLoadFailedView();
            return;
        }

        getMovieDetailTask = new GetMovieDetailTask();
        getMovieDetailTask.execute(movieId);
    }

    private void showLoadingView() {
        contentLayout.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.INVISIBLE);
        pbIndicator.setVisibility(View.VISIBLE);
    }

    private void showLoadFailedView() {
        contentLayout.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.VISIBLE);
        pbIndicator.setVisibility(View.INVISIBLE);
    }

    private void showContentView(MovieDetail movieDetail) {
        contentLayout.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.INVISIBLE);
        pbIndicator.setVisibility(View.INVISIBLE);

        NetWorkUtils.loadImage(poster, movieDetail.poster_path);
        title.setText(movieDetail.title);
        release.setText(movieDetail.release_date);
        rate.setText(String.valueOf(movieDetail.vote_average));
        overview.setText(movieDetail.overview);
    }

    public class GetMovieDetailTask extends AsyncTask<Long, Void, MovieDetail> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingView();
        }

        @Override
        protected MovieDetail doInBackground(Long... longs) {
            long movieId = INVALID_MOVIE_ID;
            if (longs.length >= 1) {
                movieId = longs[0];
            }

            if (movieId != INVALID_MOVIE_ID) {
                String responseFromHttpUrl = NetWorkUtils.getResponseFromHttpUrl(
                        NetWorkUtils.getDetailURL(String.valueOf(movieId)));
                if (!TextUtils.isEmpty(responseFromHttpUrl)) {
                    return NetWorkUtils.parseData(responseFromHttpUrl, MovieDetail.class);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieDetail movieDetail) {
            if (movieDetail != null) {
                showContentView(movieDetail);
            } else {
                showLoadFailedView();
            }
        }
    }
}
