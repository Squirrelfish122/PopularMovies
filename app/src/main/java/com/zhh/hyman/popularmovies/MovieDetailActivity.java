package com.zhh.hyman.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhh.hyman.popularmovies.data.MovieItem;
import com.zhh.hyman.popularmovies.utils.NetWorkUtils;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String PARAM_KEY_MOVIE_ITEM = "param_key_movie_item";

    private MovieItem movieItem;

    private ScrollView contentLayout;

    private ImageView poster;

    private TextView title;
    private TextView release;
    private TextView rate;
    private TextView overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initView();

        Intent intent = getIntent();
        if (intent.hasExtra(PARAM_KEY_MOVIE_ITEM)) {
            movieItem = intent.getParcelableExtra(PARAM_KEY_MOVIE_ITEM);
        }
        Log.i(TAG,"movieItem = "+ movieItem);

        showContentView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        contentLayout = (ScrollView) findViewById(R.id.movie_detail_content_layout);
        poster = (ImageView) findViewById(R.id.movie_detail_poster);
        title = (TextView) findViewById(R.id.movie_detail_title);
        release = (TextView) findViewById(R.id.movie_detail_release_date);
        rate = (TextView) findViewById(R.id.movie_detail_rate);
        overview = (TextView) findViewById(R.id.movie_detail_overview);
    }

    private void showContentView() {
        contentLayout.setVisibility(View.VISIBLE);

        NetWorkUtils.loadImage(poster, movieItem.poster_path);
        title.setText(movieItem.title);
        release.setText(movieItem.release_date);
        rate.setText(String.valueOf(movieItem.vote_average));
        overview.setText(movieItem.overview);
    }

}
